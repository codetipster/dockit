package com.dockit.core.model

// Base documentation info for both Java and Kotlin
data class DocumentationInfo(
    val description: String,
    val params: Map<String, String> = emptyMap(),
    val returns: String? = null,
    val throws: Map<String, String> = emptyMap(),
    val see: List<String> = emptyList(),
    val since: String? = null,
    val deprecated: String? = null
)

// Enhanced documentation info for Kotlin
data class KotlinDocumentationInfo(
    val baseDocumentation: DocumentationInfo,
    val propertyDocs: Map<String, String> = emptyMap(),
    val samples: List<String> = emptyList(),
    val constructorDoc: String? = null,
    val authors: List<String> = emptyList()
)

data class AnnotationInfo(
    val name: String,
    val attributes: Map<String, String> = emptyMap()
)

data class ClassInfo(
    val name: String,
    val packageName: String,
    val methods: List<MethodInfo>,
    val fields: List<FieldInfo>,
    val dependencies: List<String>,
    val superClass: String? = null,
    val interfaces: List<String> = emptyList(),
    val typeParameters: List<String> = emptyList(),
    val annotations: List<AnnotationInfo> = emptyList(),
    val documentation: DocumentationInfo? = null,
    // Kotlin-specific fields
    val kotlinDocumentation: KotlinDocumentationInfo? = null,
    val nestedClasses: List<ClassInfo> = emptyList(),
    val companionObject: CompanionObjectInfo? = null,
    val objects: List<ObjectInfo> = emptyList(),
    val isData: Boolean = false,
    val isSealed: Boolean = false,
    val isInner: Boolean = false
)

data class MethodInfo(
    val name: String,
    val returnType: String,
    val parameters: List<ParameterInfo>,
    val modifiers: List<String>,
    val typeParameters: List<String> = emptyList(),
    val annotations: List<AnnotationInfo> = emptyList(),
    val documentation: DocumentationInfo? = null,
    val accessesField: Set<String> = emptySet(),
    val localVariables: Set<String> = emptySet(),
    val methodInvocations: List<String> = emptyList(),
    val throws: List<String> = emptyList(),
    // Kotlin-specific fields
    val kotlinDocumentation: KotlinDocumentationInfo? = null,
    val receiverType: String? = null,
    val isOperator: Boolean = false,
    val isInfix: Boolean = false,
    val isInline: Boolean = false,
    val isSuspend: Boolean = false
)

data class FieldInfo(
    val name: String,
    val type: String,
    val modifiers: List<String>,
    val annotations: List<AnnotationInfo> = emptyList(),
    val documentation: DocumentationInfo? = null,
    val initializer: String? = null,
    // Kotlin-specific fields
    val kotlinDocumentation: KotlinDocumentationInfo? = null,
    val hasCustomGetter: Boolean = false,
    val hasCustomSetter: Boolean = false,
    val getterVisibility: String? = null,
    val setterVisibility: String? = null,
    val isLateinit: Boolean = false,
    val delegate: PropertyDelegateInfo? = null
)

data class ParameterInfo(
    val name: String,
    val type: String,
    val annotations: List<AnnotationInfo> = emptyList(),
    val hasDefault: Boolean = false,
    val isVararg: Boolean = false
)

data class CompanionObjectInfo(
    val name: String?,
    val fields: List<FieldInfo>,
    val methods: List<MethodInfo>,
    val annotations: List<AnnotationInfo> = emptyList()
)

data class ObjectInfo(
    val name: String,
    val fields: List<FieldInfo>,
    val methods: List<MethodInfo>,
    val annotations: List<AnnotationInfo> = emptyList()
)

data class PropertyDelegateInfo(
    val type: String,
    val expression: String
)