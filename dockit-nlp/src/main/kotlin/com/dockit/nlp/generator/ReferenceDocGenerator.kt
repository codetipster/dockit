package com.dockit.nlp.generator

import com.dockit.core.model.ClassInfo
import com.dockit.core.model.MethodInfo
import com.dockit.core.model.FieldInfo
import com.dockit.nlp.api.GptClient
import com.dockit.nlp.model.ReferenceDoc

class ReferenceDocGenerator(
    private val gptClient: GptClient
) {
    suspend fun generateDocumentation(classInfo: ClassInfo): ReferenceDoc {
        val contentBuilder = StringBuilder()

        // Class Header and Enhanced Description
        contentBuilder.appendLine("# ${classInfo.name}")
        val classDescription = generateClassDescription(classInfo)
        contentBuilder.appendLine()
        contentBuilder.appendLine(classDescription)

        // Methods Section with Enhanced Documentation
        if (classInfo.methods.isNotEmpty()) {
            contentBuilder.appendLine()
            contentBuilder.appendLine("## Methods")
            classInfo.methods.forEach { method ->
                contentBuilder.appendLine()
                contentBuilder.append(generateMethodDocumentation(classInfo, method))
            }
        }

        // Properties Section with Enhanced Documentation
        if (classInfo.fields.isNotEmpty()) {
            contentBuilder.appendLine()
            contentBuilder.appendLine("## Properties")
            classInfo.fields.forEach { field ->
                contentBuilder.appendLine()
                contentBuilder.append(generateFieldDocumentation(classInfo, field))
            }
        }

        return ReferenceDoc(contentBuilder.toString())
    }

    private suspend fun generateClassDescription(classInfo: ClassInfo): String {
        val prompt = """
            Given this class information:
            Name: ${classInfo.name}
            Package: ${classInfo.packageName}
            Original Documentation: ${classInfo.documentation?.description ?: "None"}
            
            Generate a comprehensive class description that:
            1. Explains the class's main purpose and responsibilities
            2. Describes its role in the codebase
            3. Includes any important patterns or principles it implements
            4. Notes key relationships with other components
            
            Make it clear and focused on helping developers understand when and how to use this class.
        """.trimIndent()

        return gptClient.generateDocumentation(prompt)
    }

    private suspend fun generateMethodDocumentation(classInfo: ClassInfo, method: MethodInfo): String {
        val builder = StringBuilder()

        val prompt = """
            Given this method information:
            Class: ${classInfo.name}
            Method: ${method.name}
            Return Type: ${method.returnType}
            Parameters: ${method.parameters.joinToString { "${it.name}: ${it.type}" }}
            Original Documentation: ${method.documentation?.description ?: "None"}
            
            Generate comprehensive method documentation including:
            1. Clear description of the method's purpose
            2. Parameter explanations
            3. Return value details
            4. A practical usage example
            5. Important notes about behavior
        """.trimIndent()

        val enhancedDoc = gptClient.generateDocumentation(prompt)

        builder.appendLine("### ${method.name}")
        builder.appendLine()
        builder.appendLine(enhancedDoc)

        // Parameters Section
        if (method.parameters.isNotEmpty()) {
            builder.appendLine()
            builder.appendLine("Parameters:")
            method.parameters.forEach { param ->
                val paramDescription = method.documentation?.params?.get(param.name) ?: "No description available"
                builder.appendLine("- `${param.name}` (${param.type}): $paramDescription")
            }
        }

        // Return Type Section
        method.documentation?.returns?.let { returns ->
            builder.appendLine()
            builder.appendLine("Returns: ${method.returnType} - $returns")
        }

        return builder.toString()
    }

    private suspend fun generateFieldDocumentation(classInfo: ClassInfo, field: FieldInfo): String {
        val builder = StringBuilder()

        val prompt = """
            Given this property information:
            Class: ${classInfo.name}
            Property: ${field.name}
            Type: ${field.type}
            Modifiers: ${field.modifiers.joinToString()}
            Original Documentation: ${field.documentation?.description ?: "None"}
            
            Generate comprehensive property documentation including:
            1. Clear description of the property's purpose
            2. How and when it's used
            3. Any important side effects or implications
            4. Usage considerations
        """.trimIndent()

        val enhancedDoc = gptClient.generateDocumentation(prompt)

        builder.appendLine("### ${field.name}")
        builder.appendLine("- Type: ${field.type}")
        if (field.modifiers.isNotEmpty()) {
            builder.appendLine("- Modifiers: ${field.modifiers.joinToString()}")
        }
        builder.appendLine()
        builder.appendLine(enhancedDoc)

        return builder.toString()
    }
}