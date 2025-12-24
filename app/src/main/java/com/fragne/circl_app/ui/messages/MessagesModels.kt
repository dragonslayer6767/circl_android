package com.fragne.circl_app.ui.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network User Model - Someone you can message
 */
@Serializable
data class NetworkUser(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val company: String = "",
    val bio: String = "",
    @SerialName("profile_image")
    val profileImage: String? = null,
    val tags: List<String> = emptyList(),
    @SerialName("is_online")
    val isOnline: Boolean = false
)

/**
 * Message Model - Individual message in a conversation
 */
@Serializable
data class Message(
    val id: Int,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    val content: String,
    val timestamp: String,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("video_url")
    val videoUrl: String? = null
)

/**
 * Chat Message Model - For display in chat view
 */
@Serializable
data class ChatMessage(
    val id: Int,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    val content: String,
    val timestamp: String,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("video_url")
    val videoUrl: String? = null,
    @SerialName("sender_name")
    val senderName: String? = null,
    @SerialName("sender_image")
    val senderImage: String? = null
)

/**
 * Conversation - Grouped messages with a user
 */
data class Conversation(
    val user: NetworkUser,
    val messages: List<Message>,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val hasUnread: Boolean
)

/**
 * API Response Models
 */
@Serializable
data class APIMessage(
    val id: Int,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("receiver_id")
    val receiverId: Int,
    val content: String,
    val timestamp: String,
    @SerialName("is_read")
    val isRead: Boolean
)

@Serializable
data class APINetworkUser(
    val id: Int,
    val name: String,
    val email: String,
    val username: String? = null,
    @SerialName("profile_image")
    val profileImage: String? = null
)

/**
 * Messages UI State
 */
data class MessagesUiState(
    val conversations: List<Conversation> = emptyList(),
    val networkUsers: List<NetworkUser> = emptyList(),
    val searchText: String = "",
    val suggestedUsers: List<NetworkUser> = emptyList(),
    val selectedUser: NetworkUser? = null,
    val isLoading: Boolean = false,
    val userProfileImageUrl: String = "",
    val userFirstName: String = "",
    val unreadMessageCount: Int = 0,
    val errorMessage: String? = null
)

/**
 * Chat UI State
 */
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val messageText: String = "",
    val isTyping: Boolean = false,
    val isSending: Boolean = false,
    val showOptionsMenu: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Send Message Request
 */
@Serializable
data class SendMessageRequest(
    @SerialName("receiver_id")
    val receiverId: Int,
    val content: String
)

/**
 * Mark as Read Request
 */
@Serializable
data class MarkAsReadRequest(
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("receiver_id")
    val receiverId: Int
)

