package com.dockit.core.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class JavaCodeParserTest {

    private val parser = JavaCodeParser()

    @Test
    fun `parse should extract class name correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
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
            package com.example;
            
            public class TestClass {
                // Class content
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("com.example", result?.packageName)
    }

    @Test
    fun `parse should extract methods correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
                public void testMethod() {
                    // Method content
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(1, result?.methods?.size)
        assertEquals("testMethod", result?.methods?.get(0)?.name)
    }

    @Test
    fun `parse should extract fields correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
                private int testField;
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(1, result?.fields?.size)
        assertEquals("testField", result?.fields?.get(0)?.name)
    }
}
