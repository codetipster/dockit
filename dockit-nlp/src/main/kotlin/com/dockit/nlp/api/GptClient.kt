// dockit-nlp/src/main/kotlin/com/dockit/nlp/api/GptClient.kt
package com.dockit.nlp.api

import com.dockit.nlp.api.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface GptApi {
    @POST("v1/chat/completions")
    suspend fun generateCompletion(
        @Header("Authorization") auth: String,
        @Body request: GptRequest
    ): GptResponse
}

class GptClient(private val config: GptConfig) {
    private val api: GptApi

    init {
        // Add logging for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GptApi::class.java)
    }

    suspend fun generateDocumentation(prompt: String): String {
        try {
            val request = GptRequest(
                model = config.model,
                messages = listOf(
                    Message(
                        role = "system",
                        content = "You are a technical documentation expert. Generate clear, concise, and comprehensive reference documentation."
                    ),
                    Message(
                        role = "user",
                        content = prompt
                    )
                ),
                temperature = config.temperature,
                max_tokens = config.maxTokens
            )

            val response = api.generateCompletion(
                "Bearer ${config.apiKey}",
                request
            )

            return response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No response content")

        } catch (e: Exception) {
            println("Error details: ${e.message}")
            throw e
        }
    }
}