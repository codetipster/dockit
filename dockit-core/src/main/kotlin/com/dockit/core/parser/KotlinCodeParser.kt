package com.dockit.core.parser

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.*
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
                dependencies = extractDependencies(psiFile)
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
                    modifiers = extractMethodModifiers(function)
                )
            }
    }

    private fun extractParameters(function: KtNamedFunction): List<ParameterInfo> {
        return function.valueParameters.map { param ->
            ParameterInfo(
                name = param.name ?: "",
                type = param.typeReference?.text ?: "Any"
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
                    modifiers = extractPropertyModifiers(property)
                )
            }
    }

    private fun extractPropertyModifiers(property: KtProperty): List<String> {
        val modifiers = mutableListOf<String>()

        property.modifierList?.text
            ?.split(" ")
            ?.filter { it.isNotBlank() }
            ?.forEach { modifiers.add(it) }

        // Add val/var modifier
        if (property.isVar) {
            modifiers.add("var")
        } else {
            modifiers.add("val")
        }

        return modifiers
    }

    private fun extractDependencies(psiFile: KtFile): List<String> {
        return psiFile.importDirectives
            .mapNotNull { it.importedFqName?.asString() }
            .distinct()
            .sorted()
    }
}