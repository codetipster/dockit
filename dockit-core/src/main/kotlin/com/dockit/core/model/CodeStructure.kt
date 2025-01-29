package com.dockit.core.model

data class ClassInfo(
    val name: String,
    val packageName: String,
    val methods: List<MethodInfo>,
    val fields: List<FieldInfo>,
    val dependencies: List<String>
)

data class MethodInfo(
    val name: String,
    val returnType: String,
    val parameters: List<ParameterInfo>,
    val modifiers: List<String>
)

data class FieldInfo(
    val name: String,
    val type: String,
    val modifiers: List<String>
)

data class ParameterInfo(
    val name: String,
    val type: String
)
