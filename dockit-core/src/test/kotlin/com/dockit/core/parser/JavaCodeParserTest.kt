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
    fun `parse should extract methods with parameters and return types correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
                public String testMethod(int param1, String param2) {
                    return "";
                }
                
                private void anotherMethod() {
                    // Method content
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(2, result?.methods?.size)

        val method1 = result?.methods?.get(0)
        assertEquals("testMethod", method1?.name)
        assertEquals("String", method1?.returnType)
        assertEquals(listOf("public"), method1?.modifiers)
        assertEquals(2, method1?.parameters?.size)
        assertEquals("param1", method1?.parameters?.get(0)?.name)
        assertEquals("int", method1?.parameters?.get(0)?.type)
        assertEquals("param2", method1?.parameters?.get(1)?.name)
        assertEquals("String", method1?.parameters?.get(1)?.type)

        val method2 = result?.methods?.get(1)
        assertEquals("anotherMethod", method2?.name)
        assertEquals("void", method2?.returnType)
        assertEquals(listOf("private"), method2?.modifiers)
        assertTrue(method2?.parameters?.isEmpty() == true)
    }

    @Test
    fun `parse should extract fields with modifiers correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
                private int testField;
                protected String anotherField;
                public static final long CONSTANT_FIELD = 42L;
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(3, result?.fields?.size)

        val field1 = result?.fields?.get(0)
        assertEquals("testField", field1?.name)
        assertEquals("int", field1?.type)
        assertEquals(listOf("private"), field1?.modifiers)

        val field2 = result?.fields?.get(1)
        assertEquals("anotherField", field2?.name)
        assertEquals("String", field2?.type)
        assertEquals(listOf("protected"), field2?.modifiers)

        val field3 = result?.fields?.get(2)
        assertEquals("CONSTANT_FIELD", field3?.name)
        assertEquals("long", field3?.type)
        assertEquals(listOf("public", "static", "final"), field3?.modifiers)
    }

    @Test
    fun `parse should extract dependencies correctly`() {
        val sourceCode = """
            package com.example;
            
            import java.util.List;
            import java.util.Map;
            import com.example.other.SomeClass;
            
            public class TestClass {
                private List<String> list;
                private Map<String, Integer> map;
                private SomeClass someClass;
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        val expectedDependencies = listOf(
            "java.util.List",
            "java.util.Map",
            "com.example.other.SomeClass"
        )
        assertEquals(expectedDependencies.sorted(), result?.dependencies?.sorted())
    }

    @Test
    fun `parse should handle class without package declaration`() {
        val sourceCode = """
            public class TestClass {
                private String field;
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