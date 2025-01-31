package com.dockit.core.parser

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.psiUtil.*
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.kdoc.psi.impl.KDocSection
import org.jetbrains.kotlin.kdoc.psi.impl.KDocTag
import com.dockit.core.model.*

class KotlinCodeParser {
    fun parse(sourceCode: String): ClassInfo? {
        val disposable = Disposer.newDisposable()

        try {
            val environment = KotlinCoreEnvironment.createForProduction(
                disposable,
                CompilerConfiguration(),
                EnvironmentConfigFiles.JVM_CONFIG_FILES
            )

            val psiFile = KtPsiFactory(environment.project).createFile(sourceCode)
            val ktClass = psiFile.declarations.filterIsInstance<KtClass>().firstOrNull() ?: return null

            return ClassInfo(
                name = ktClass.name ?: "",
                packageName = psiFile.packageFqName.asString(),
                methods = extractMethods(ktClass),
                fields = extractFields(ktClass),
                dependencies = extractDependencies(psiFile),
                documentation = extractDocumentation(ktClass),
                kotlinDocumentation = extractKotlinDocumentation(ktClass),
                typeParameters = extractTypeParameters(ktClass),
                nestedClasses = extractNestedClasses(ktClass),
                companionObject = extractCompanionObject(ktClass),
                objects = extractObjects(ktClass),
                isData = ktClass.isData(),
                isSealed = ktClass.isSealed(),
                isInner = ktClass.isInner()
            )
        } catch (e: Exception) {
            return null
        } finally {
            Disposer.dispose(disposable)
        }
    }

    private fun extractMethods(ktClass: KtClass): List<MethodInfo> {
        return ktClass.declarations
            .filterIsInstance<KtNamedFunction>()
            .map { function ->
                MethodInfo(
                    name = function.name ?: "",
                    returnType = function.typeReference?.text ?: "Unit",
                    parameters = extractParameters(function),
                    modifiers = extractMethodModifiers(function),
                    documentation = extractDocumentation(function),
                    kotlinDocumentation = extractKotlinDocumentation(function),
                    typeParameters = function.typeParameters.map { it.name ?: "" },
                    receiverType = function.receiverTypeReference?.text,
                    isOperator = checkModifier(function, KtTokens.OPERATOR_KEYWORD),
                    isInfix = checkModifier(function, KtTokens.INFIX_KEYWORD),
                    isInline = checkModifier(function, KtTokens.INLINE_KEYWORD),
                    isSuspend = checkModifier(function, KtTokens.SUSPEND_KEYWORD)
                )
            }
    }

    private fun extractParameters(function: KtNamedFunction): List<ParameterInfo> {
        return function.valueParameters.map { param ->
            ParameterInfo(
                name = param.name ?: "",
                type = param.typeReference?.text ?: "Any",
                annotations = extractAnnotations(param),
                hasDefault = param.defaultValue != null,
                isVararg = param.isVarArg
            )
        }
    }

    private fun extractMethodModifiers(function: KtNamedFunction): List<String> {
        return function.modifierList?.text
            ?.split(" ")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

    private fun extractFields(ktClass: KtClass): List<FieldInfo> {
        return ktClass.declarations
            .filterIsInstance<KtProperty>()
            .map { property ->
                FieldInfo(
                    name = property.name ?: "",
                    type = property.typeReference?.text ?: "Any",
                    modifiers = extractPropertyModifiers(property),
                    documentation = extractDocumentation(property),
                    kotlinDocumentation = extractKotlinDocumentation(property),
                    initializer = property.initializer?.text,
                    hasCustomGetter = property.getter != null,  // Changed this line
                    hasCustomSetter = property.setter != null,  // Changed this line
                    getterVisibility = property.getter?.let { extractVisibility(it) },
                    setterVisibility = property.setter?.let { extractVisibility(it) },
                    isLateinit = checkModifier(property, KtTokens.LATEINIT_KEYWORD),
                    delegate = property.delegate?.let { delegate ->
                        PropertyDelegateInfo(
                            type = extractDelegateType(delegate),
                            expression = delegate.expression?.text ?: ""
                        )
                    }
                )
            }
    }

    private fun KtPropertyAccessor.hasBody(): Boolean {
        return bodyBlockExpression != null || bodyExpression != null
    }

    private fun extractDelegateType(delegate: KtPropertyDelegate): String {
        val expression = delegate.expression?.text ?: return ""
        return when {
            expression.startsWith("lazy") -> "lazy"
            expression.contains("Delegates.") -> expression.substringAfter("Delegates.").substringBefore("(")
            else -> expression.substringBefore("{").substringBefore("(").trim()
        }
    }

    private fun extractPropertyModifiers(property: KtProperty): List<String> {
        val modifiers = mutableListOf<String>()

        property.modifierList?.text
            ?.split(" ")
            ?.filter { it.isNotBlank() }
            ?.forEach { modifiers.add(it) }

        if (property.isVar) {
            modifiers.add("var")
        } else {
            modifiers.add("val")
        }

        return modifiers
    }

    private fun extractVisibility(element: KtDeclaration): String? {
        return element.modifierList?.text
            ?.split(" ")
            ?.firstOrNull { it in setOf("public", "private", "protected", "internal") }
    }

    private fun extractTypeParameters(ktClass: KtClass): List<String> {
        return ktClass.typeParameters.map { param ->
            buildString {
                append("<")
                if (checkModifier(param, KtTokens.OUT_KEYWORD)) {
                    append("out ")
                }
                append(param.name)
                param.extendsBound?.let { bound ->
                    if (!checkModifier(param, KtTokens.OUT_KEYWORD)) {
                        append(" extends ")
                        append(bound.text)
                    }
                }
                append(">")
            }
        }
    }

    private fun extractDocumentation(element: KtElement): DocumentationInfo? {
        val kdoc = (element as? KtDeclaration)?.docComment ?: return null

        return DocumentationInfo(
            description = extractKDocDescription(kdoc),
            params = extractKDocTags(kdoc, "param"),
            returns = extractKDocTag(kdoc, "return"),
            throws = extractKDocTags(kdoc, "throws"),
            see = extractKDocTagsList(kdoc, "see"),
            since = extractKDocTag(kdoc, "since"),
            deprecated = extractKDocTag(kdoc, "deprecated")
        )
    }

    private fun extractKotlinDocumentation(element: KtElement): KotlinDocumentationInfo? {
        val documentation = extractDocumentation(element) ?: return null
        val kdoc = (element as? KtDeclaration)?.docComment ?: return null

        return KotlinDocumentationInfo(
            baseDocumentation = documentation,
            propertyDocs = extractKDocTags(kdoc, "property"),
            samples = extractKDocTagsList(kdoc, "sample"),
            constructorDoc = extractKDocTag(kdoc, "constructor"),
            authors = extractKDocTagsList(kdoc, "author")
        )
    }

    private fun extractKDocDescription(kdoc: KDoc): String {
        return kdoc.getContent()
            .lines()
            .takeWhile { !it.trimStart().startsWith('@') }
            .joinToString("\n")
            .trim()
    }

    private fun extractKDocTags(kdoc: KDoc, tagName: String): Map<String, String> {
        return kdoc.text.lines()
            .filter { it.trim().startsWith("* @$tagName") || it.trim().startsWith("@$tagName") }
            .mapNotNull { line ->
                val content = line.substringAfter("@$tagName").trim()
                val parts = content.split(" ", limit = 2)
                if (parts.size == 2) {
                    parts[0].trim() to parts[1].trim()
                } else {
                    null
                }
            }
            .toMap()
    }

    private fun extractKDocTagsList(kdoc: KDoc, tagName: String): List<String> {
        return kdoc.text.lines()
            .filter { it.trim().startsWith("* @$tagName") || it.trim().startsWith("@$tagName") }
            .map { it.substringAfter("@$tagName").trim() }
    }

    private fun extractKDocTag(kdoc: KDoc, tagName: String): String? {
        return kdoc.text.lines()
            .find { it.trim().startsWith("* @$tagName") || it.trim().startsWith("@$tagName") }
            ?.substringAfter("@$tagName")
            ?.trim()
    }

    private fun extractDependencies(psiFile: KtFile): List<String> {
        return psiFile.importDirectives
            .mapNotNull { it.importedFqName?.asString() }
            .distinct()
            .sorted()
    }

    private fun extractNestedClasses(ktClass: KtClass): List<ClassInfo> {
        return ktClass.declarations
            .filterIsInstance<KtClass>()
            .map { parse(it.text) }
            .filterNotNull()
    }

    private fun extractCompanionObject(ktClass: KtClass): CompanionObjectInfo? {
        return ktClass.companionObjects.firstOrNull()?.let { companion ->
            CompanionObjectInfo(
                name = companion.name,
                fields = extractFieldsFromObject(companion),
                methods = extractMethodsFromObject(companion),
                annotations = extractAnnotations(companion)
            )
        }
    }

    private fun extractFieldsFromObject(obj: KtObjectDeclaration): List<FieldInfo> {
        return obj.declarations
            .filterIsInstance<KtProperty>()
            .map { property ->
                val type = when {
                    // For const properties, infer type from initializer
                    property.hasModifier(KtTokens.CONST_KEYWORD) -> {
                        when {
                            property.initializer?.text?.contains("\"") == true -> "String"
                            property.initializer?.text?.endsWith("L") == true -> "Long"
                            property.initializer?.text?.contains(".") == true -> "Double"
                            else -> "Int"
                        }
                    }
                    // Otherwise use declared type or fallback
                    else -> property.typeReference?.text ?: "Any"
                }

                FieldInfo(
                    name = property.name ?: "",
                    type = type,
                    modifiers = extractPropertyModifiers(property),
                    documentation = extractDocumentation(property),
                    kotlinDocumentation = extractKotlinDocumentation(property),
                    initializer = property.initializer?.text,
                    hasCustomGetter = property.getter != null,
                    hasCustomSetter = property.setter != null,
                    getterVisibility = property.getter?.let { extractVisibility(it) },
                    setterVisibility = property.setter?.let { extractVisibility(it) },
                    isLateinit = checkModifier(property, KtTokens.LATEINIT_KEYWORD),
                    delegate = property.delegate?.let { delegate ->
                        PropertyDelegateInfo(
                            type = extractDelegateType(delegate),
                            expression = delegate.expression?.text ?: ""
                        )
                    }
                )
            }
    }


    private fun extractMethodsFromObject(obj: KtObjectDeclaration): List<MethodInfo> {
        return obj.declarations
            .filterIsInstance<KtNamedFunction>()
            .map { function ->
                MethodInfo(
                    name = function.name ?: "",
                    returnType = function.typeReference?.text ?: "Unit",
                    parameters = extractParameters(function),
                    modifiers = extractMethodModifiers(function),
                    documentation = extractDocumentation(function),
                    kotlinDocumentation = extractKotlinDocumentation(function),
                    typeParameters = function.typeParameters.map { it.name ?: "" },
                    receiverType = function.receiverTypeReference?.text,
                    isOperator = checkModifier(function, KtTokens.OPERATOR_KEYWORD),
                    isInfix = checkModifier(function, KtTokens.INFIX_KEYWORD),
                    isInline = checkModifier(function, KtTokens.INLINE_KEYWORD),
                    isSuspend = checkModifier(function, KtTokens.SUSPEND_KEYWORD)
                )
            }
    }

    private fun extractObjects(ktClass: KtClass): List<ObjectInfo> {
        return ktClass.declarations
            .filterIsInstance<KtObjectDeclaration>()
            .filter { !it.isCompanion() }
            .map { obj ->
                ObjectInfo(
                    name = obj.name ?: "",
                    fields = extractFieldsFromObject(obj),
                    methods = extractMethodsFromObject(obj),
                    annotations = extractAnnotations(obj)
                )
            }
    }

    private fun extractAnnotations(element: KtAnnotated): List<AnnotationInfo> {
        return element.annotationEntries.map { annotation ->
            AnnotationInfo(
                name = annotation.typeReference?.text?.removePrefix("@") ?: "",
                attributes = annotation.valueArguments.associate { arg ->
                    val name = arg.getArgumentName()?.asName?.toString() ?: "value"
                    name to (arg.getArgumentExpression()?.text ?: "")
                }
            )
        }
    }

    private fun checkModifier(element: KtModifierListOwner, token: KtModifierKeywordToken): Boolean {
        return element.modifierList?.hasModifier(token) ?: false
    }

    private fun KDoc.getContent(): String {
        return this.text
            .lines()
            .dropWhile { it.trim().startsWith("/**") }
            .dropLastWhile { it.trim().startsWith("*/") }
            .joinToString("\n")
            .trim()
    }
}