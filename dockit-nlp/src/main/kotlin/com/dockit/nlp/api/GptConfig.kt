package com.dockit.nlp.api

data class GptConfig(
    val apiKey: String,
    val model: String = "gpt-4",
    val maxTokens: Int = 2000,
    val temperature: Double = 0.3
)