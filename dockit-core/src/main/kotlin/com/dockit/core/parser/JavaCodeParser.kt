package com.dockit.core.parser

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.*
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.javadoc.JavadocBlockTag
import com.github.javaparser.javadoc.Javadoc
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.comments.JavadocComment
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations
import com.github.javaparser.ParserConfiguration
import com.dockit.core.model.*

class JavaCodeParser {
    private val parser: JavaParser

    init {
        // Initialize symbol solver for advanced type resolution
        val typeSolver = CombinedTypeSolver(ReflectionTypeSolver())
        val symbolSolver = JavaSymbolSolver(typeSolver)

        // Create configuration and set the symbol solver
        val configuration = ParserConfiguration()
            .setSymbolResolver(symbolSolver)

        parser = JavaParser(configuration)
    }

    fun parse(sourceCode: String): ClassInfo? {
        return try {
            val compilationUnit: CompilationUnit = parser.parse(sourceCode).result.orElse(null) ?: return null
            val classDeclaration = compilationUnit.findFirst(ClassOrInterfaceDeclaration::class.java).orElse(null)
                ?: return null

            ClassInfo(
                name = classDeclaration.nameAsString,
                packageName = compilationUnit.packageDeclaration.map { it.nameAsString }.orElse(""),
                methods = extractMethods(classDeclaration),
                fields = extractFields(classDeclaration),
                dependencies = extractDependencies(compilationUnit, classDeclaration),
                superClass = extractSuperClass(classDeclaration),
                interfaces = extractInterfaces(classDeclaration),
                typeParameters = extractTypeParameters(classDeclaration),
                annotations = extractAnnotations(classDeclaration),
                documentation = extractDocumentation(classDeclaration)
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun extractMethods(classDeclaration: ClassOrInterfaceDeclaration): List<MethodInfo> {
        return classDeclaration.methods.map { method ->
            MethodInfo(
                name = method.nameAsString,
                returnType = method.type.asString(),
                parameters = extractParameters(method),
                modifiers = method.modifiers.map { it.keyword.asString().lowercase() },
                typeParameters = method.typeParameters.map { it.asString() },
                annotations = extractAnnotations(method),
                documentation = extractDocumentation(method),
                accessesField = extractFieldAccesses(method),
                localVariables = extractLocalVariables(method),
                methodInvocations = extractMethodInvocations(method),
                throws = method.thrownExceptions.map { it.asString() }
            )
        }
    }

    private fun extractParameters(method: MethodDeclaration): List<ParameterInfo> {
        return method.parameters.map { param ->
            ParameterInfo(
                name = param.nameAsString,
                type = param.type.asString(),
                annotations = extractAnnotations(param)
            )
        }
    }

    private fun extractFields(classDeclaration: ClassOrInterfaceDeclaration): List<FieldInfo> {
        return classDeclaration.fields.flatMap { field ->
            field.variables.map { variable ->
                FieldInfo(
                    name = variable.nameAsString,
                    type = field.elementType.asString(),
                    modifiers = field.modifiers.map { it.keyword.asString().lowercase() },
                    annotations = extractAnnotations(field),
                    documentation = extractDocumentation(field),
                    initializer = variable.initializer.map { it.toString() }.orElse(null)
                )
            }
        }
    }

    private fun extractSuperClass(classDeclaration: ClassOrInterfaceDeclaration): String? {
        return classDeclaration.extendedTypes.firstOrNull()?.nameAsString
    }

    private fun extractInterfaces(classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        return classDeclaration.implementedTypes.map { it.toString() }
    }

    private fun extractTypeParameters(classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        return classDeclaration.typeParameters.map { it.asString() }
    }

    private fun extractAnnotations(node: NodeWithAnnotations<*>): List<AnnotationInfo> {
        return node.annotations.map { annotation ->
            AnnotationInfo(
                name = annotation.nameAsString,
                attributes = extractAnnotationAttributes(annotation)
            )
        }
    }

    private fun extractAnnotationAttributes(annotation: AnnotationExpr): Map<String, String> {
        return when (annotation) {
            is SingleMemberAnnotationExpr -> mapOf("value" to annotation.memberValue.toString().trim('"'))
            is NormalAnnotationExpr -> annotation.pairs.associate {
                it.nameAsString to it.value.toString().trim('"')
            }
            else -> emptyMap()
        }
    }

    private fun extractDocumentation(node: NodeWithJavadoc<*>): DocumentationInfo? {
        return node.javadoc.map { javadoc ->
            DocumentationInfo(
                description = javadoc.description.toText(),
                params = extractJavadocParams(javadoc),
                returns = extractJavadocReturns(javadoc),
                throws = extractJavadocThrows(javadoc),
                see = extractJavadocSeeAlso(javadoc),
                since = extractJavadocSince(javadoc),
                deprecated = extractJavadocDeprecated(javadoc)
            )
        }.orElse(null)
    }

    private fun extractJavadocParams(javadoc: Javadoc): Map<String, String> {
        return javadoc.blockTags
            .filter { it.type == JavadocBlockTag.Type.PARAM }
            .associate { tag ->
                val name = tag.name.orElse("")
                val description = tag.content.toText().trim()
                name to description
            }
    }

    private fun extractJavadocReturns(javadoc: Javadoc): String? {
        return javadoc.blockTags
            .firstOrNull { it.type == JavadocBlockTag.Type.RETURN }
            ?.content?.toText()?.trim()
    }

    private fun extractJavadocThrows(javadoc: Javadoc): Map<String, String> {
        return javadoc.blockTags
            .filter { it.type == JavadocBlockTag.Type.THROWS }
            .associate { tag ->
                val exceptionType = tag.name.orElse("")
                val description = tag.content.toText().trim()
                exceptionType to description
            }
    }

    private fun extractJavadocSeeAlso(javadoc: Javadoc): List<String> {
        return javadoc.blockTags
            .filter { it.type == JavadocBlockTag.Type.SEE }
            .map { it.content.toText().trim() }
    }

    private fun extractJavadocSince(javadoc: Javadoc): String? {
        return javadoc.blockTags
            .firstOrNull { it.type == JavadocBlockTag.Type.SINCE }
            ?.content?.toText()?.trim()
    }

    private fun extractJavadocDeprecated(javadoc: Javadoc): String? {
        return javadoc.blockTags
            .firstOrNull { it.type == JavadocBlockTag.Type.DEPRECATED }
            ?.content?.toText()?.trim()
    }

    private fun extractDependencies(compilationUnit: CompilationUnit, classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        val explicitImports = compilationUnit.imports.map { it.nameAsString }
        val methodDependencies = extractMethodDependencies(classDeclaration)
        val fieldDependencies = extractFieldDependencies(classDeclaration)
        val annotationDependencies = extractAnnotationDependencies(classDeclaration)

        return (explicitImports + methodDependencies + fieldDependencies + annotationDependencies)
            .filter { it.contains(".") } // Only include fully qualified names
            .distinct()
            .sorted()
    }

    private fun extractMethodDependencies(classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        val dependencies = mutableListOf<String>()

        classDeclaration.methods.forEach { method ->
            // Return type dependency
            if (method.type.toString().contains(".")) {
                dependencies.add(method.type.toString())
            }

            // Parameter type dependencies
            method.parameters.forEach { param ->
                if (param.type.toString().contains(".")) {
                    dependencies.add(param.type.toString())
                }
            }

            // Method body dependencies
            method.findAll(MethodCallExpr::class.java).forEach { methodCall ->
                try {
                    val resolvedMethod = methodCall.resolve()
                    dependencies.add(resolvedMethod.declaringType().qualifiedName)
                } catch (e: Exception) {
                    // Skip if resolution fails
                }
            }

            // Exception dependencies
            method.thrownExceptions.forEach { exception ->
                if (exception.toString().contains(".")) {
                    dependencies.add(exception.toString())
                }
            }
        }

        return dependencies
    }

    private fun extractFieldDependencies(classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        val dependencies = mutableListOf<String>()

        classDeclaration.fields.forEach { field ->
            // Field type dependency
            if (field.elementType.toString().contains(".")) {
                dependencies.add(field.elementType.toString())
            }

            // Field initializer dependencies
            field.variables.forEach { variable ->
                variable.initializer.ifPresent { initializer ->
                    initializer.findAll(ObjectCreationExpr::class.java).forEach { creation ->
                        if (creation.type.toString().contains(".")) {
                            dependencies.add(creation.type.toString())
                        }
                    }
                }
            }
        }

        return dependencies
    }

    private fun extractAnnotationDependencies(classDeclaration: ClassOrInterfaceDeclaration): List<String> {
        val dependencies = mutableListOf<String>()

        // Class annotations
        dependencies.addAll(
            classDeclaration.annotations
                .map { it.nameAsString }
                .filter { it.contains(".") }
        )

        // Method annotations
        classDeclaration.methods.forEach { method ->
            dependencies.addAll(
                method.annotations
                    .map { it.nameAsString }
                    .filter { it.contains(".") }
            )
        }

        // Field annotations
        classDeclaration.fields.forEach { field ->
            dependencies.addAll(
                field.annotations
                    .map { it.nameAsString }
                    .filter { it.contains(".") }
            )
        }

        return dependencies
    }

    private fun extractMethodInvocations(method: MethodDeclaration): List<String> {
        val invocations = mutableListOf<String>()

        method.findAll(MethodCallExpr::class.java).forEach { methodCall ->
            try {
                val resolvedMethod = methodCall.resolve()
                val declaringType = resolvedMethod.declaringType().qualifiedName

                // Special handling for System.out.println
                if (methodCall.scope.isPresent &&
                    methodCall.scope.get().toString() == "System.out" &&
                    methodCall.nameAsString == "println") {
                    invocations.add("System.out.println")
                } else {
                    invocations.add("$declaringType.${methodCall.nameAsString}")
                }
            } catch (e: Exception) {
                // Fallback to raw method call expression
                methodCall.scope.ifPresent { scope ->
                    invocations.add("${scope}.${methodCall.nameAsString}")
                }
            }
        }

        return invocations.distinct()
    }

    private fun extractFieldAccesses(method: MethodDeclaration): Set<String> {
        val accesses = mutableSetOf<String>()
        val classDecl = method.findAncestor(ClassOrInterfaceDeclaration::class.java).orElse(null) ?: return accesses

        // Get all field names from the class
        val fieldNames = classDecl.fields
            .flatMap { field -> field.variables.map { it.nameAsString } }
            .toSet()

        println("Found fields in class: $fieldNames")

        // Check scope expressions for field access
        method.findAll(MethodCallExpr::class.java).forEach { methodCall ->
            methodCall.scope.ifPresent { scope ->
                val scopeName = scope.toString()
                println("Checking method call scope: $scopeName")
                if (fieldNames.contains(scopeName)) {
                    println("Found field access in method call: $scopeName")
                    accesses.add(scopeName)
                }
            }
        }

        // Check direct field references
        method.findAll(NameExpr::class.java).forEach { nameExpr ->
            println("Checking name expression: ${nameExpr.nameAsString}")
            if (fieldNames.contains(nameExpr.nameAsString)) {
                println("Found direct field access: ${nameExpr.nameAsString}")
                accesses.add(nameExpr.nameAsString)
            }
        }

        // Check explicit field access
        method.findAll(FieldAccessExpr::class.java).forEach { fieldAccess ->
            println("Checking field access: ${fieldAccess.nameAsString}")
            if (fieldNames.contains(fieldAccess.nameAsString)) {
                println("Found explicit field access: ${fieldAccess.nameAsString}")
                accesses.add(fieldAccess.nameAsString)
            }
        }

        // Additional check for assignment targets
        method.findAll(AssignExpr::class.java).forEach { assignExpr ->
            val target = assignExpr.target
            if (target is NameExpr && fieldNames.contains(target.nameAsString)) {
                println("Found field assignment: ${target.nameAsString}")
                accesses.add(target.nameAsString)
            }
        }

        // Check variable declarators for field usage
        method.findAll(VariableDeclarator::class.java).forEach { varDecl ->
            varDecl.initializer.ifPresent { init ->
                init.findAll(NameExpr::class.java).forEach { nameExpr ->
                    if (fieldNames.contains(nameExpr.nameAsString)) {
                        println("Found field use in variable declaration: ${nameExpr.nameAsString}")
                        accesses.add(nameExpr.nameAsString)
                    }
                }
            }
        }

        println("Final field accesses found: $accesses")
        return accesses
    }


    private fun extractLocalVariables(method: MethodDeclaration): Set<String> {
        return method.findAll(VariableDeclarator::class.java)
            .map { it.nameAsString }
            .filter { name ->
                method.findAll(NameExpr::class.java)
                    .any { it.nameAsString == name }
            }
            .toSet()
    }
}