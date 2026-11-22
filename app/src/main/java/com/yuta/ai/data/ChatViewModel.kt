package com.yuta.ai.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID

data class UiMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String, // "user" or "assistant"
    val text: String,
    val isError: Boolean = false
)

private const val YUTA_SYSTEM_PROMPT = """
You are Yuta, a warm, witty, sharp AI companion living inside the Yuta AI app.
Reply in the same language the user writes in (Hindi, Hinglish, or English).
Keep answers natural, confident, and concise unless the user asks for depth.
You were brought to life by your creator/developer to be genuinely useful, never robotic.
"""

class ChatViewModel : ViewModel() {

    val messages = mutableStateListOf<UiMessage>()
    val isTyping = mutableStateOf(false)

    fun sendMessage(text: String) {
        if (text.isBlank() || isTyping.value) return

        messages.add(UiMessage(role = "user", text = text))
        isTyping.value = true

        viewModelScope.launch {
            val history = messages.map {
                PollinationsApi.ChatMessage(
                    role = if (it.role == "user") "user" else "assistant",
                    content = it.text
                )
            }

            PollinationsApi.chat(history, YUTA_SYSTEM_PROMPT)
                .onSuccess { reply ->
                    messages.add(UiMessage(role = "assistant", text = reply))
                }
                .onFailure { err ->
                    messages.add(
                        UiMessage(
                            role = "assistant",
                            text = err.message ?: "Kuch gadbad ho gayi, dobara try karo.",
                            isError = true
                        )
                    )
                }

            isTyping.value = false
        }
    }

    fun clearChat() {
        messages.clear()
    }
}
