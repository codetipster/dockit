package com.dockit.nlp.generator

import com.dockit.core.model.*
import com.dockit.nlp.api.GptClient
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ReferenceDocGeneratorTest {

    private val mockGptClient = mockk<GptClient>()
    private val generator = ReferenceDocGenerator(mockGptClient)

    @Test
    fun `should generate reference documentation for a simple class`() = runBlocking {
        // Mock GPT responses
        coEvery {
            mockGptClient.generateDocumentation(any())
        } returns "Enhanced documentation from GPT"

        // Given a class analyzed by dockit-core
        val classInfo = ClassInfo(
            name = "Calculator",
            packageName = "com.example.math",
            methods = listOf(
                MethodInfo(
                    name = "add",
                    returnType = "Int",
                    parameters = listOf(
                        ParameterInfo("a", "Int"),
                        ParameterInfo("b", "Int")
                    ),
                    modifiers = listOf("public"),
                    documentation = DocumentationInfo(
                        description = "Adds two numbers together",
                        params = mapOf(
                            "a" to "First number",
                            "b" to "Second number"
                        ),
                        returns = "Sum of the two numbers"
                    )
                )
            ),
            fields = listOf(
                FieldInfo(
                    name = "precision",
                    type = "Int",
                    modifiers = listOf("private"),
                    documentation = DocumentationInfo(
                        description = "The decimal precision for calculations"
                    )
                )
            ),
            documentation = DocumentationInfo(
                description = "A simple calculator for basic arithmetic operations"
            ),
            dependencies = emptyList()
        )

        // When generating documentation
        val documentation = generator.generateDocumentation(classInfo)

        // Then should contain expected sections
        assertNotNull(documentation)
        assertTrue(documentation.content.contains("# Calculator"))
        assertTrue(documentation.content.contains("Enhanced documentation from GPT"))
        assertTrue(documentation.content.contains("## Methods"))
        assertTrue(documentation.content.contains("### add"))
        assertTrue(documentation.content.contains("## Properties"))
        assertTrue(documentation.content.contains("### precision"))
    }
}