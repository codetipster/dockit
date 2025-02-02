package com.dockit

import com.dockit.core.parser.JavaCodeParser
import com.dockit.core.parser.KotlinCodeParser
import com.dockit.nlp.api.GptClient
import com.dockit.nlp.api.GptConfig
import com.dockit.nlp.generator.ReferenceDocGenerator
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.util.Properties
import java.io.FileInputStream
import java.io.File

fun loadApiKey(): String {
    val properties = Properties()
    val configFile = File("dockit-nlp/src/main/resources/config.properties")

    if (!configFile.exists()) {
        throw IllegalStateException("""
           config.properties file not found!
           Please create the file at: ${configFile.absolutePath}
           And add your OpenAI API key as: openai.api.key=your-api-key-here
       """.trimIndent())
    }

    properties.load(FileInputStream(configFile))
    return properties.getProperty("openai.api.key") ?: throw IllegalStateException("API key not found in config.properties")
}

fun main() = runBlocking {
    try {
        // Initialize parsers
        val javaParser = JavaCodeParser()
        val kotlinParser = KotlinCodeParser()

        // Initialize GPT client with config from properties
        val gptConfig = GptConfig(
            apiKey = loadApiKey()
        )
        val gptClient = GptClient(gptConfig)

        // Initialize documentation generator
        val docGenerator = ReferenceDocGenerator(gptClient)

        // Directory containing source code to document - start with dockit's own code
        val sourceDir = File("dockit-core/src/main/kotlin/com/dockit/core")

        if (!sourceDir.exists()) {
            throw IllegalStateException("Source directory not found: ${sourceDir.absolutePath}")
        }

        println("Starting documentation generation...")
        println("Source directory: ${sourceDir.absolutePath}")

        // Create output directory
        val outputDir = File("generated-docs")
        outputDir.mkdirs()
        println("Output directory: ${outputDir.absolutePath}")

        // Process all source files
        var processedFiles = 0
        sourceDir.walk()
            .filter { it.isFile && (it.extension == "java" || it.extension == "kt") }
            .forEach { file ->
                println("\nProcessing file: ${file.name}")

                try {
                    // Parse the file
                    val classInfo = when(file.extension) {
                        "java" -> {
                            println("Parsing Java file...")
                            javaParser.parse(file.readText())
                        }
                        "kt" -> {
                            println("Parsing Kotlin file...")
                            kotlinParser.parse(file.readText())
                        }
                        else -> null
                    }

                    // Generate documentation
                    classInfo?.let { info ->
                        println("Generating documentation for: ${info.name}")
                        val documentation = docGenerator.generateDocumentation(info)

                        // Write documentation to file
                        val outputFile = File(outputDir, "${info.name}.md")
                        outputFile.writeText(documentation.content)
                        println("Documentation generated: ${outputFile.absolutePath}")
                        processedFiles++
                    }
                } catch (e: Exception) {
                    println("Error processing file ${file.name}: ${e.message}")
                }
            }

        println("\nDocumentation generation completed!")
        println("Files processed: $processedFiles")
        println("Generated documentation can be found in: ${outputDir.absolutePath}")

    } catch (e: Exception) {
        println("Error during documentation generation: ${e.message}")
        e.printStackTrace()
    }
}