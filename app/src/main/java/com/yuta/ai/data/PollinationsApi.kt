package com.yuta.ai.data

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

/**
 * Thin client around Pollinations.ai's free, keyless endpoints.
 * No API key, no account, no payment — this is what keeps Yuta AI free to run.
 * Docs: https://github.com/pollinations/pollinations/blob/master/APIDOCS.md
 *
 * These are shared, anonymous endpoints, so they can occasionally return a
 * rate-limit (402) or a transient server error. We try the richer multi-turn
 * endpoint first and silently fall back to the simpler one-shot endpoint so a
 * single hiccup doesn't surface as a dead chat.
 */
object PollinationsApi {

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    data class ChatMessage(val role: String, val content: String)

    private const val USER_AGENT = "YutaAI/1.0 (Android; +https://github.com)"

    suspend fun chat(history: List<ChatMessage>, systemPrompt: String): Result<String> =
        runCatching {
            try {
                chatViaOpenAiEndpoint(history, systemPrompt)
            } catch (primaryError: Exception) {
                // Primary endpoint hiccuped — try the simpler one-shot endpoint before giving up.
                try {
                    chatViaSimpleEndpoint(history, systemPrompt)
                } catch (_: Exception) {
                    throw primaryError
                }
            }
        }

    private fun chatViaOpenAiEndpoint(history: List<ChatMessage>, systemPrompt: String): String {
        val messages = JSONArray()
        messages.put(JSONObject().put("role", "system").put("content", systemPrompt))
        history.forEach {
            messages.put(JSONObject().put("role", it.role).put("content", it.content))
        }

        val body = JSONObject().apply {
            put("model", "openai")
            put("messages", messages)
            put("private", true)
            put("seed", System.currentTimeMillis() % 100000)
        }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://text.pollinations.ai/openai")
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Referer", "https://yuta.ai")
            .post(body)
            .build()

        client.newCall(request).execute().use { resp ->
            val raw = resp.body?.string().orEmpty().trim()
            if (!resp.isSuccessful) {
                throw ApiException(resp.code, friendlyError(resp.code))
            }
            // OpenAI-style response: { choices: [ { message: { content: "..." } } ] }
            val extracted = runCatching {
                JSONObject(raw)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            }.getOrNull()

            return (extracted ?: raw).trim().ifBlank {
                "Hmm, kuch samajh nahi aaya. Ek baar phir se poocho?"
            }
        }
    }

    private fun chatViaSimpleEndpoint(history: List<ChatMessage>, systemPrompt: String): String {
        val lastUserMessage = history.lastOrNull { it.role == "user" }?.content.orEmpty()
        val transcript = history.dropLast(1).joinToString("\n") { "${it.role}: ${it.content}" }
        val fullSystem = if (transcript.isBlank()) systemPrompt else
            "$systemPrompt\n\nEarlier in this conversation:\n$transcript"

        val body = JSONObject().apply {
            put("messages", JSONArray().apply {
                put(JSONObject().put("role", "system").put("content", fullSystem))
                put(JSONObject().put("role", "user").put("content", lastUserMessage))
            })
            put("model", "openai")
        }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://text.pollinations.ai/")
            .addHeader("User-Agent", USER_AGENT)
            .addHeader("Referer", "https://yuta.ai")
            .post(body)
            .build()

        client.newCall(request).execute().use { resp ->
            val raw = resp.body?.string().orEmpty().trim()
            if (!resp.isSuccessful) {
                throw ApiException(resp.code, friendlyError(resp.code))
            }
            val extracted = runCatching { JSONObject(raw).optString("response", "") }.getOrDefault("")
            return (extracted.ifBlank { raw }).trim().ifBlank {
                "Hmm, kuch samajh nahi aaya. Ek baar phir se poocho?"
            }
        }
    }

    private fun friendlyError(code: Int): String = when (code) {
        402, 429 -> "Abhi thodi der ke liye free quota khatam ho gaya hai — 30-40 second ruk ke phir try karo."
        in 500..599 -> "Yuta ka server side thoda busy hai, ek baar phir se try karo."
        else -> "Request fail ho gayi (code $code). Internet check karke dobara try karo."
    }

    /**
     * Builds a direct image URL from Pollinations' free keyless image endpoint.
     * Loading this URL (e.g. with Coil) generates the image on demand — no download step needed.
     */
    fun imageUrl(
        prompt: String,
        width: Int = 1024,
        height: Int = 1024,
        seed: Long = System.currentTimeMillis() % 1_000_000,
        model: String = "flux"
    ): String {
        // A small, free quality boost: ask Pollinations' own LLM to enrich the prompt,
        // and nudge the model towards sharper, more detailed results.
        val qualityPrompt = "$prompt, highly detailed, sharp focus, professional quality"
        val encoded = URLEncoder.encode(qualityPrompt, "UTF-8").replace("+", "%20")
        return "https://image.pollinations.ai/prompt/$encoded" +
            "?width=$width&height=$height&seed=$seed&model=$model&enhance=true&nologo=true&nofeed=true"
    }

    class ApiException(val code: Int, message: String) : Exception(message)
}
