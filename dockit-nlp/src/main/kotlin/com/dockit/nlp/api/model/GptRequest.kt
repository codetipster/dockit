package com.dockit.nlp.api.model

data class GptRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double,
    val max_tokens: Int
)

data class Message(
    val role: String,
    val content: String
)

data class GptResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)