package com.dockit.core.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import com.dockit.core.model.*

class JavaCodeParserTest {
    private lateinit var parser: JavaCodeParser

    @BeforeEach
    fun setUp() {
        parser = JavaCodeParser()
    }

    @Test
    @DisplayName("Basic class parsing - name and package")
    fun `should parse basic class information correctly`() {
        val sourceCode = """
            package com.example;
            
            public class TestClass {
                // Class content
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("TestClass", result?.name)
        assertEquals("com.example", result?.packageName)
    }

    @Test
    @DisplayName("Method parsing with full details")
    fun `should parse methods with all details correctly`() {
        val sourceCode = """
            package com.example;
            
            import java.util.List;
            
            public class TestClass {
                /**
                 * Process items in the list.
                 * @param items the items to process
                 * @return processed result
                 * @throws IllegalArgumentException if items is null
                 */
                public String processItems(List<String> items) throws IllegalArgumentException {
                    if (items == null) {
                        throw new IllegalArgumentException("Items cannot be null");
                    }
                    return items.toString();
                }
                
                private void helperMethod() {
                    // Helper implementation
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(2, result?.methods?.size)

        val processMethod = result?.methods?.find { it.name == "processItems" }
        assertNotNull(processMethod)
        assertEquals("String", processMethod?.returnType)
        assertEquals(listOf("public"), processMethod?.modifiers)
        assertEquals(1, processMethod?.parameters?.size)
        assertEquals("items", processMethod?.parameters?.get(0)?.name)
        assertEquals("List<String>", processMethod?.parameters?.get(0)?.type)
        assertEquals(listOf("IllegalArgumentException"), processMethod?.throws)

        val helperMethod = result?.methods?.find { it.name == "helperMethod" }
        assertNotNull(helperMethod)
        assertEquals("void", helperMethod?.returnType)
        assertEquals(listOf("private"), helperMethod?.modifiers)
        assertTrue(helperMethod?.parameters?.isEmpty() == true)
    }

    @Test
    @DisplayName("Field parsing with annotations and documentation")
    fun `should parse fields with annotations and documentation correctly`() {
        val sourceCode = """
            package com.example;
            
            import javax.persistence.Id;
            import javax.persistence.Column;
            
            public class TestClass {
                @Id
                private Long id;
                
                /**
                 * The name field.
                 */
                @Column(name = "user_name")
                private String name;
                
                @Deprecated
                public static final int OLD_CONSTANT = 42;
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals(3, result?.fields?.size)

        val idField = result?.fields?.find { it.name == "id" }
        assertNotNull(idField)
        assertEquals("Long", idField?.type)
        assertEquals(listOf("private"), idField?.modifiers)
        assertEquals("Id", idField?.annotations?.first()?.name)

        val nameField = result?.fields?.find { it.name == "name" }
        assertNotNull(nameField)
        assertTrue(nameField?.documentation?.description?.contains("name field") == true)
        assertEquals("Column", nameField?.annotations?.first()?.name)
        assertEquals("user_name", nameField?.annotations?.first()?.attributes?.get("name"))

        val constantField = result?.fields?.find { it.name == "OLD_CONSTANT" }
        assertNotNull(constantField)
        assertEquals(listOf("public", "static", "final"), constantField?.modifiers)
        assertEquals("42", constantField?.initializer)
    }

    @Test
    @DisplayName("Inheritance and interface implementation")
    fun `should parse inheritance and interface implementations correctly`() {
        val sourceCode = """
            package com.example;
            
            import java.io.Serializable;
            import java.util.Comparable;
            
            public class TestClass extends BaseClass implements Serializable, Comparable<TestClass> {
                @Override
                public int compareTo(TestClass other) {
                    return 0;
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertEquals("BaseClass", result?.superClass)
        assertEquals(listOf("Serializable", "Comparable<TestClass>").sorted(), result?.interfaces?.sorted())
    }

    @Test
    @DisplayName("Method body analysis")
    fun `should analyze method body correctly`() {
        val sourceCode = """
        package com.example;
        
        import java.util.List;
        import java.util.ArrayList;
        
        public class TestClass {
            private List<String> items = new ArrayList<>();
            
            public void processItems() {
                String localVar = "test";
                items.add(localVar);
                System.out.println(items.size());
            }
        }
    """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result, "Parse result should not be null")

        val method = result?.methods?.first()
        assertNotNull(method, "Method should not be null")

        println("Found fields: ${result?.fields?.map { it.name }}")
        println("Method accesses fields: ${method?.accessesField}")
        println("Method local variables: ${method?.localVariables}")
        println("Method invocations: ${method?.methodInvocations}")

        assertTrue(method?.accessesField?.contains("items") == true,
            "Should detect 'items' field access, found: ${method?.accessesField}")
        assertTrue(method?.localVariables?.contains("localVar") == true,
            "Should detect 'localVar' local variable, found: ${method?.localVariables}")
        assertTrue(method?.methodInvocations?.any { it.contains("List.add") } == true,
            "Should detect List.add invocation, found: ${method?.methodInvocations}")
        assertTrue(method?.methodInvocations?.any { it.contains("System.out.println") } == true,
            "Should detect System.out.println invocation, found: ${method?.methodInvocations}")
    }

    @Test
    @DisplayName("Documentation and Javadoc")
    fun `should parse documentation and javadoc correctly`() {
        val sourceCode = """
            package com.example;
            
            /**
             * Test class documentation.
             * @author John Doe
             * @since 1.0
             */
            public class TestClass {
                /**
                 * Process the given value.
                 * @param value the input value
                 * @return processed result
                 * @throws IllegalArgumentException if value is invalid
                 * @see SomeOtherClass
                 * @deprecated Use newProcess instead
                 */
                public String process(String value) {
                    return value;
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)

        assertNotNull(result)
        assertNotNull(result?.documentation)
        assertTrue(result?.documentation?.description?.contains("Test class") == true)

        val method = result?.methods?.first()
        assertNotNull(method?.documentation)
        assertEquals("Process the given value.", method?.documentation?.description?.trim())
        assertEquals("the input value", method?.documentation?.params?.get("value"))
        assertNotNull(method?.documentation?.returns)
        assertEquals("if value is invalid", method?.documentation?.throws?.get("IllegalArgumentException"))
        assertTrue(method?.documentation?.see?.contains("SomeOtherClass") == true)
        assertNotNull(method?.documentation?.deprecated)
    }

    @Test
    @DisplayName("Invalid source code handling")
    fun `should handle invalid source code gracefully`() {
        val invalidCode = "this is not valid java code"
        val result = parser.parse(invalidCode)
        assertNull(result)
    }
}