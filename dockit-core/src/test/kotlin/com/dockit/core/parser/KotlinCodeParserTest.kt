package com.dockit.core.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

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

    @Test
    @DisplayName("Parse should extract KDoc documentation")
    fun `parse should extract documentation correctly`() {
        val sourceCode = """
            package com.example
            
            /**
             * A test class that demonstrates various features.
             * 
             * @property count The number of items processed
             * @constructor Creates a new instance with default settings
             */
            class TestClass(
                private val count: Int = 0
            ) {
                /**
                 * Process the given data and return results.
                 * 
                 * @param data Input data to process
                 * @return Processed result
                 * @throws IllegalArgumentException if data is invalid
                 * @see ProcessingUtils for additional utilities
                 * @sample com.example.Samples.processExample
                 * @since 1.1.0
                 */
                fun process(data: String): Result<String> {
                    if (data.isEmpty()) {
                        throw IllegalArgumentException("Data cannot be empty")
                    }
                    return Result.success(data)
                }
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result)

        val classDocumentation = result?.documentation
        assertNotNull(classDocumentation)
        assertTrue(classDocumentation?.description?.contains("demonstrates various features") == true)

        val method = result?.methods?.first()
        val methodDocumentation = method?.documentation
        assertNotNull(methodDocumentation)
        assertTrue(methodDocumentation?.description?.contains("Process the given data") == true)
        assertEquals("Input data to process", methodDocumentation?.params?.get("data"))
        assertNotNull(methodDocumentation?.returns)
        assertEquals("if data is invalid", methodDocumentation?.throws?.get("IllegalArgumentException"))
        assertEquals("1.1.0", methodDocumentation?.since)
    }

    @Test
    @DisplayName("Parse should handle sealed classes")
    fun `parse should handle sealed classes correctly`() {
        val sourceCode = """
        package com.example
        
        sealed class Result<out T> {
            data class Success<T>(val value: T): Result<T>()
            data class Error(val message: String): Result<Nothing>()
        }
    """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result)
        assertTrue(result?.isSealed == true)
        assertEquals("<out T>", result?.typeParameters?.first())

        val nestedClasses = result?.nestedClasses
        assertNotNull(nestedClasses)
        assertEquals(2, nestedClasses?.size)

        val successClass = nestedClasses?.find { it.name == "Success" }
        assertNotNull(successClass)
        assertTrue(successClass?.isData == true)
    }

    @Test
    @DisplayName("Parse should handle data classes with companion objects")
    fun `parse should handle data classes correctly`() {
        val sourceCode = """
        package com.example
        
        data class User(
            val id: Long,
            var name: String
        ) {
            companion object {
                const val TABLE_NAME = "users"
            }
            
            object Permissions {
                fun check(action: String): Boolean = true
            }
        }
    """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result)
        assertTrue(result?.isData == true)

        val companionObj = result?.companionObject
        assertNotNull(companionObj)
        val tableNameField = companionObj?.fields?.find { it.name == "TABLE_NAME" }
        assertNotNull(tableNameField)
        assertEquals("String", tableNameField?.type)

        val permissions = result?.objects?.firstOrNull()
        assertNotNull(permissions)
        assertEquals("Permissions", permissions?.name)
        assertEquals(1, permissions?.methods?.size)
    }

    @Test
    @DisplayName("Parse should handle Kotlin property features")
    fun `parse should handle kotlin property features correctly`() {
        val sourceCode = """
        package com.example
        
        class TestClass {
            var counter: Int = 0
                private set
                get() = field
        }
    """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result)

        val counterProp = result?.fields?.firstOrNull()
        assertNotNull(counterProp)
        assertTrue(counterProp?.hasCustomGetter == true)
        assertTrue(counterProp?.hasCustomSetter == true)
        assertEquals("private", counterProp?.setterVisibility)

        // Test computed property separately
        val computedSource = """
        package com.example
        
        class TestClass {
            val status: String
                get() = "Active"
        }
    """.trimIndent()

        val computedResult = parser.parse(computedSource)
        val statusProp = computedResult?.fields?.firstOrNull()
        assertNotNull(statusProp)
        assertTrue(statusProp?.hasCustomGetter == true)
        assertFalse(statusProp?.hasCustomSetter == true)

        // Test delegated property separately
        val delegatedSource = """
        package com.example
        
        class TestClass {
            val computed by lazy { "Value" }
        }
    """.trimIndent()

        val delegatedResult = parser.parse(delegatedSource)
        val computedProp = delegatedResult?.fields?.firstOrNull()
        assertNotNull(computedProp)
        assertNotNull(computedProp?.delegate)
        assertEquals("lazy", computedProp?.delegate?.type)
    }

    @Test
    @DisplayName("Parse should handle Kotlin function features")
    fun `parse should handle kotlin function features correctly`() {
        val sourceCode = """
            package com.example
            
            class TestClass {
                // Extension function
                fun String.addTimestamp(): String = "$this-${System.currentTimeMillis()}"
                
                // Infix function
                infix fun combine(other: TestClass): TestClass = TestClass()
                
                // Operator function
                operator fun plus(other: TestClass): TestClass = TestClass()
                
                // Function with type parameters and default values
                fun <T> process(input: T, callback: (T) -> Unit = {}) {
                    callback(input)
                }
                
                // Suspend function
                suspend fun fetchData(): String = "data"
            }
        """.trimIndent()

        val result = parser.parse(sourceCode)
        assertNotNull(result)

        val extensionMethod = result?.methods?.find { it.name == "addTimestamp" }
        assertNotNull(extensionMethod)
        assertEquals("String", extensionMethod?.receiverType)

        val infixMethod = result?.methods?.find { it.name == "combine" }
        assertNotNull(infixMethod)
        assertTrue(infixMethod?.isInfix == true)

        val operatorMethod = result?.methods?.find { it.name == "plus" }
        assertNotNull(operatorMethod)
        assertTrue(operatorMethod?.isOperator == true)

        val processMethod = result?.methods?.find { it.name == "process" }
        assertNotNull(processMethod)
        assertEquals(listOf("T"), processMethod?.typeParameters)
        assertTrue(processMethod?.parameters?.get(1)?.hasDefault == true)

        val suspendMethod = result?.methods?.find { it.name == "fetchData" }
        assertNotNull(suspendMethod)
        assertTrue(suspendMethod?.isSuspend == true)
    }
}