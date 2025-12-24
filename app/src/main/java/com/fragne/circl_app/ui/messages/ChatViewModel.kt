package com.fragne.circl_app.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for Chat Screen
 * Manages individual chat conversation
 */
class ChatViewModel(
    private val user: NetworkUser,
    private val initialMessages: List<Message>
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val currentUserId = "1" // TODO: Get from UserDefaults/DataStore

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            // Convert Message to ChatMessage with sender info
            val chatMessages = initialMessages.map { message ->
                ChatMessage(
                    id = message.id,
                    senderId = message.senderId,
                    receiverId = message.receiverId,
                    content = message.content,
                    timestamp = message.timestamp,
                    isRead = message.isRead,
                    imageUrl = message.imageUrl,
                    videoUrl = message.videoUrl,
                    senderName = if (message.senderId == currentUserId) "You" else user.name,
                    senderImage = if (message.senderId == currentUserId) null else user.profileImage
                )
            }

            _uiState.update { it.copy(messages = chatMessages.sortedBy { msg -> msg.timestamp }) }
        }
    }

    fun updateMessageText(text: String) {
        _uiState.update { it.copy(messageText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.messageText.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }

            try {
                // TODO: Replace with actual API call
                delay(500)

                // Create new message
                val newMessage = ChatMessage(
                    id = System.currentTimeMillis().toInt(),
                    senderId = currentUserId,
                    receiverId = user.id,
                    content = text,
                    timestamp = formatTimestamp(System.currentTimeMillis()),
                    isRead = false,
                    senderName = "You",
                    senderImage = null
                )

                // Add to messages list
                val updatedMessages = _uiState.value.messages + newMessage

                _uiState.update { it.copy(
                    messages = updatedMessages,
                    messageText = "",
                    isSending = false
                )}

                // Simulate receiving a reply after a delay
                simulateReply()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isSending = false,
                    errorMessage = "Failed to send message: ${e.message}"
                )}
            }
        }
    }

    private fun simulateReply() {
        viewModelScope.launch {
            delay(2000)

            val replies = listOf(
                "Thanks for reaching out!",
                "I'll get back to you on that.",
                "Sounds good!",
                "Let me check and get back to you.",
                "Absolutely, let's do it!"
            )

            val replyMessage = ChatMessage(
                id = System.currentTimeMillis().toInt(),
                senderId = user.id,
                receiverId = currentUserId,
                content = replies.random(),
                timestamp = formatTimestamp(System.currentTimeMillis()),
                isRead = false,
                senderName = user.name,
                senderImage = user.profileImage
            )

            val updatedMessages = _uiState.value.messages + replyMessage

            _uiState.update { it.copy(messages = updatedMessages) }
        }
    }

    fun toggleOptionsMenu() {
        _uiState.update { it.copy(showOptionsMenu = !it.showOptionsMenu) }
    }

    fun closeOptionsMenu() {
        _uiState.update { it.copy(showOptionsMenu = false) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun formatTimestamp(timeInMillis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(timeInMillis))
    }
}

/**
 * Factory for creating ChatViewModel with parameters
 */
class ChatViewModelFactory(
    private val user: NetworkUser,
    private val messages: List<Message>
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(user, messages) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

