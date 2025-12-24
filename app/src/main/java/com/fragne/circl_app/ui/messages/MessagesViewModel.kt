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
 * ViewModel for Messages Screen
 * Manages conversations list and network users
 */
class MessagesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        // Load user info
        _uiState.update { it.copy(
            userFirstName = "John",
            userProfileImageUrl = ""
        )}

        fetchNetworkUsers()
        fetchMessages()
    }

    fun updateSearchText(text: String) {
        _uiState.update { it.copy(searchText = text) }

        if (text.isEmpty()) {
            _uiState.update { it.copy(suggestedUsers = emptyList()) }
        } else {
            filterUsers(text)
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(
            searchText = "",
            suggestedUsers = emptyList(),
            selectedUser = null
        )}
    }

    fun selectUser(user: NetworkUser) {
        _uiState.update { it.copy(
            selectedUser = user,
            searchText = user.name,
            suggestedUsers = emptyList()
        )}
    }

    fun clearSelectedUser() {
        _uiState.update { it.copy(selectedUser = null) }
    }

    private fun filterUsers(query: String) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) {
            _uiState.update { it.copy(suggestedUsers = emptyList()) }
            return
        }

        val results = _uiState.value.networkUsers.filter { user ->
            user.name.lowercase().contains(q) ||
            user.username.lowercase().contains(q) ||
            user.email.lowercase().contains(q)
        }

        _uiState.update { it.copy(suggestedUsers = results) }
    }

    fun refreshMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            fetchNetworkUsers()
            delay(500)
            fetchMessages()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun fetchNetworkUsers() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(1000)

                val mockUsers = listOf(
                    NetworkUser(
                        id = "301",
                        name = "Alex Kumar",
                        username = "alexk",
                        email = "alex@business.com",
                        company = "StartupHub",
                        profileImage = null,
                        isOnline = true
                    ),
                    NetworkUser(
                        id = "302",
                        name = "Jennifer Lee",
                        username = "jenlee",
                        email = "jennifer@tech.com",
                        company = "Innovation Labs",
                        profileImage = null,
                        isOnline = false
                    ),
                    NetworkUser(
                        id = "303",
                        name = "Marcus Johnson",
                        username = "marcusj",
                        email = "marcus@venture.com",
                        company = "Venture Capital Partners",
                        profileImage = null,
                        isOnline = true
                    ),
                    NetworkUser(
                        id = "101",
                        name = "Sarah Anderson",
                        username = "saraha",
                        email = "sarah@startup.com",
                        company = "TechVenture Inc",
                        profileImage = null,
                        isOnline = false
                    ),
                    NetworkUser(
                        id = "102",
                        name = "David Chen",
                        username = "davidc",
                        email = "david@innovation.com",
                        company = "AI Solutions",
                        profileImage = null,
                        isOnline = true
                    )
                )

                _uiState.update { it.copy(networkUsers = mockUsers) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to load network: ${e.message}"
                )}
            }
        }
    }

    private fun fetchMessages() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(1000)

                // Mock messages - simulating conversations
                val now = System.currentTimeMillis()
                val currentUserId = "1" // Mock current user ID

                val mockMessages = listOf(
                    // Conversation with Alex Kumar
                    Message(
                        id = 1,
                        senderId = "301",
                        receiverId = currentUserId,
                        content = "Hey! Did you see the latest update on our project?",
                        timestamp = formatTimestamp(now - 5 * 60 * 1000),
                        isRead = false
                    ),
                    Message(
                        id = 2,
                        senderId = currentUserId,
                        receiverId = "301",
                        content = "Not yet, what's new?",
                        timestamp = formatTimestamp(now - 10 * 60 * 1000),
                        isRead = true
                    ),
                    Message(
                        id = 3,
                        senderId = "301",
                        receiverId = currentUserId,
                        content = "We got approved for the next round!",
                        timestamp = formatTimestamp(now - 2 * 60 * 1000),
                        isRead = false
                    ),

                    // Conversation with Jennifer Lee
                    Message(
                        id = 4,
                        senderId = "302",
                        receiverId = currentUserId,
                        content = "Thanks for the coffee chat yesterday!",
                        timestamp = formatTimestamp(now - 2 * 60 * 60 * 1000),
                        isRead = true
                    ),
                    Message(
                        id = 5,
                        senderId = currentUserId,
                        receiverId = "302",
                        content = "Anytime! Let's do it again soon.",
                        timestamp = formatTimestamp(now - 1 * 60 * 60 * 1000),
                        isRead = true
                    ),

                    // Conversation with Marcus Johnson
                    Message(
                        id = 6,
                        senderId = "303",
                        receiverId = currentUserId,
                        content = "I'd love to discuss your pitch deck. When are you free?",
                        timestamp = formatTimestamp(now - 24 * 60 * 60 * 1000),
                        isRead = true
                    ),
                    Message(
                        id = 7,
                        senderId = currentUserId,
                        receiverId = "303",
                        content = "How about Tuesday at 2pm?",
                        timestamp = formatTimestamp(now - 23 * 60 * 60 * 1000),
                        isRead = true
                    ),
                    Message(
                        id = 8,
                        senderId = "303",
                        receiverId = currentUserId,
                        content = "Perfect! I'll send you a calendar invite.",
                        timestamp = formatTimestamp(now - 22 * 60 * 60 * 1000),
                        isRead = false
                    )
                )

                // Group messages by conversation partner
                val groupedMessages = mockMessages.groupBy { message ->
                    if (message.senderId == currentUserId) message.receiverId else message.senderId
                }

                // Create conversations
                val conversations = groupedMessages.mapNotNull { (userId, messages) ->
                    val user = _uiState.value.networkUsers.find { it.id == userId }
                    user?.let {
                        val unreadMessages = messages.filter {
                            it.receiverId == currentUserId && !it.isRead
                        }

                        Conversation(
                            user = it,
                            messages = messages.sortedBy { msg -> msg.timestamp },
                            lastMessage = messages.lastOrNull()?.content ?: "",
                            timestamp = messages.lastOrNull()?.timestamp ?: "",
                            unreadCount = unreadMessages.size,
                            hasUnread = unreadMessages.isNotEmpty()
                        )
                    }
                }.sortedByDescending { it.timestamp }

                val totalUnread = conversations.sumOf { it.unreadCount }

                _uiState.update { it.copy(
                    conversations = conversations,
                    unreadMessageCount = totalUnread
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to load messages: ${e.message}"
                )}
            }
        }
    }

    fun markAsRead(senderId: String) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(300)

                // Update local state
                val updatedConversations = _uiState.value.conversations.map { conversation ->
                    if (conversation.user.id == senderId) {
                        val updatedMessages = conversation.messages.map { message ->
                            if (message.senderId == senderId) {
                                message.copy(isRead = true)
                            } else message
                        }
                        conversation.copy(
                            messages = updatedMessages,
                            unreadCount = 0,
                            hasUnread = false
                        )
                    } else conversation
                }

                val totalUnread = updatedConversations.sumOf { it.unreadCount }

                _uiState.update { it.copy(
                    conversations = updatedConversations,
                    unreadMessageCount = totalUnread
                )}
            } catch (e: Exception) {
                println("Error marking messages as read: ${e.message}")
            }
        }
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

