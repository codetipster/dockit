package com.dockit.core.parser

import com.dockit.core.model.*
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.FieldDeclaration

class JavaCodeParser {
    private val parser = JavaParser()

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
                dependencies = extractDependencies(compilationUnit)
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
                modifiers = method.modifiers.map { it.keyword.asString().lowercase() }
            )
        }
    }

    private fun extractParameters(method: MethodDeclaration): List<ParameterInfo> {
        return method.parameters.map { param ->
            ParameterInfo(
                name = param.nameAsString,
                type = param.type.asString()
            )
        }
    }

    private fun extractFields(classDeclaration: ClassOrInterfaceDeclaration): List<FieldInfo> {
        return classDeclaration.fields.flatMap { field ->
            field.variables.map { variable ->
                FieldInfo(
                    name = variable.nameAsString,
                    type = field.elementType.asString(),
                    modifiers = field.modifiers.map { it.keyword.asString().lowercase() }
                )
            }
        }
    }

    private fun extractDependencies(compilationUnit: CompilationUnit): List<String> {
        return compilationUnit.imports
            .map { it.nameAsString }
            .distinct()
            .sorted()
    }
}