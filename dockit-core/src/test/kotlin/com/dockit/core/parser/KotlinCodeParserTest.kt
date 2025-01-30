package com.dockit.core.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class KotlinCodeParserTest {
    private val parser = KotlinCodeParser()

    @Test
    fun `parse should extract class name correctly`() {
        val sourceCode = """
            package com.example
            
            class TestClass {
                // Class content
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("TestClass", result?.name)
    }

    @Test
    fun `parse should extract package name correctly`() {
        val sourceCode = """
            package com.example
            
            class TestClass {
                // Class content
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("com.example", result?.packageName)
    }

    @Test
    fun `parse should extract methods with parameters and return types correctly`() {
        val sourceCode = """
            package com.example
            
            class TestClass {
                fun testMethod(param1: Int, param2: String): String {
                    return ""
                }
                
                private fun anotherMethod() {
                    // Method content
                }
                
                suspend fun asyncMethod(): Int {
                    return 0
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(3, result?.methods?.size)

        val method1 = result?.methods?.get(0)
        assertEquals("testMethod", method1?.name)
        assertEquals("String", method1?.returnType)
        assertTrue(method1?.modifiers?.isEmpty() == true)
        assertEquals(2, method1?.parameters?.size)
        assertEquals("param1", method1?.parameters?.get(0)?.name)
        assertEquals("Int", method1?.parameters?.get(0)?.type)
        assertEquals("param2", method1?.parameters?.get(1)?.name)
        assertEquals("String", method1?.parameters?.get(1)?.type)

        val method2 = result?.methods?.get(1)
        assertEquals("anotherMethod", method2?.name)
        assertEquals("Unit", method2?.returnType)
        assertEquals(listOf("private"), method2?.modifiers)
        assertTrue(method2?.parameters?.isEmpty() == true)

        val method3 = result?.methods?.get(2)
        assertEquals("asyncMethod", method3?.name)
        assertEquals("Int", method3?.returnType)
        assertEquals(listOf("suspend"), method3?.modifiers)
    }

    @Test
    fun `parse should extract properties correctly`() {
        val sourceCode = """
            package com.example
            
            class TestClass {
                private val readOnlyProp: Int = 0
                var mutableProp: String = ""
                lateinit var lateInitProp: Boolean
                const val CONSTANT_PROP: Long = 42L
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(4, result?.fields?.size)

        val field1 = result?.fields?.get(0)
        assertEquals("readOnlyProp", field1?.name)
        assertEquals("Int", field1?.type)
        assertEquals(listOf("private", "val"), field1?.modifiers)

        val field2 = result?.fields?.get(1)
        assertEquals("mutableProp", field2?.name)
        assertEquals("String", field2?.type)
        assertEquals(listOf("var"), field2?.modifiers)

        val field3 = result?.fields?.get(2)
        assertEquals("lateInitProp", field3?.name)
        assertEquals("Boolean", field3?.type)
        assertEquals(listOf("lateinit", "var"), field3?.modifiers)

        val field4 = result?.fields?.get(3)
        assertEquals("CONSTANT_PROP", field4?.name)
        assertEquals("Long", field4?.type)
        assertEquals(listOf("const", "val"), field4?.modifiers)
    }

    @Test
    fun `parse should extract dependencies correctly`() {
        val sourceCode = """
            package com.example
            
            import kotlin.collections.List
            import kotlin.collections.Map
            import com.example.other.SomeClass
            
            class TestClass {
                private val list: List<String> = listOf()
                private val map: Map<String, Int> = mapOf()
                private val someClass: SomeClass? = null
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        val expectedDependencies = listOf(
            "kotlin.collections.List",
            "kotlin.collections.Map",
            "com.example.other.SomeClass"
        )
        assertEquals(expectedDependencies.sorted(), result?.dependencies?.sorted())
    }

    @Test
    fun `parse should handle class without package declaration`() {
        val sourceCode = """
            class TestClass {
                private val field: String = ""
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("", result?.packageName)
        assertEquals("TestClass", result?.name)
    }

    @Test
    fun `parse should return null for invalid source code`() {
        val sourceCode = "invalid code"
        val result = parser.parse(sourceCode)
        assertNull(result)
    }
}