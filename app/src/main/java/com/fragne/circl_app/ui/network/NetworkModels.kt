package com.fragne.circl_app.ui.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network Tab enum - Maps to the 3 tabs in the network screen
 */
enum class NetworkTab(
    val title: String,
    val compactTitle: String,
    val subtitle: String,
    val icon: String
) {
    ENTREPRENEURS("Entrepreneurs", "Connect", "Growth Partners", "person.2.fill"),
    MENTORS("Mentors", "Mentors", "Expert Guidance", "graduationcap.fill"),
    MY_NETWORK("My Network", "My Network", "Connections", "person.3.fill");

    companion object {
        fun fromString(value: String): NetworkTab {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: ENTREPRENEURS
        }
    }
}

/**
 * Entrepreneur Profile Model
 */
@Serializable
data class EntrepreneurProfile(
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val email: String,
    @SerialName("profile_image")
    val profileImage: String? = null,
    @SerialName("business_name")
    val businessName: String = "",
    @SerialName("business_stage")
    val businessStage: String = "",
    @SerialName("business_industry")
    val businessIndustry: String = "",
    val tags: List<String> = emptyList(),
    val bio: String = "",
    @SerialName("is_verified")
    val isVerified: Boolean = false
)

/**
 * Mentor Profile Model
 */
@Serializable
data class MentorProfile(
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val email: String,
    @SerialName("profile_image")
    val profileImage: String? = null,
    val expertise: List<String> = emptyList(),
    val experience: String = "",
    val company: String = "",
    val title: String = "",
    @SerialName("mentorship_areas")
    val mentorshipAreas: List<String> = emptyList(),
    val bio: String = "",
    @SerialName("is_verified")
    val isVerified: Boolean = false,
    @SerialName("rating")
    val rating: Float = 0f,
    @SerialName("sessions_completed")
    val sessionsCompleted: Int = 0
)

/**
 * Network Connection Model - Someone in your network
 */
@Serializable
data class NetworkConnection(
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val email: String,
    @SerialName("profile_image")
    val profileImage: String? = null,
    @SerialName("connection_type")
    val connectionType: String = "friend", // friend, mentor, mentee
    @SerialName("connected_since")
    val connectedSince: String,
    val status: String = "active", // active, pending, blocked
    @SerialName("last_message_time")
    val lastMessageTime: String? = null,
    @SerialName("unread_count")
    val unreadCount: Int = 0,
    val company: String? = null,
    val title: String? = null,
    @SerialName("is_online")
    val isOnline: Boolean = false
)

/**
 * Friend Request Model
 */
@Serializable
data class FriendRequest(
    @SerialName("request_id")
    val requestId: Int,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("sender_name")
    val senderName: String,
    @SerialName("sender_email")
    val senderEmail: String,
    @SerialName("sender_profile_image")
    val senderProfileImage: String? = null,
    val message: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    val status: String = "pending" // pending, accepted, declined
)

/**
 * Network UI State
 */
data class NetworkUiState(
    val selectedTab: NetworkTab = NetworkTab.ENTREPRENEURS,
    val entrepreneurs: List<EntrepreneurProfile> = emptyList(),
    val mentors: List<MentorProfile> = emptyList(),
    val myNetwork: List<NetworkConnection> = emptyList(),
    val pendingRequests: List<FriendRequest> = emptyList(),
    val declinedUserIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val userFirstName: String = "",
    val userProfileImageUrl: String = "",
    val unreadMessageCount: Int = 0,
    val errorMessage: String? = null,
    // Stats
    val connectionCount: Int = 0,
    val activeCount: Int = 0
)

/**
 * Full Profile Model - Detailed profile view
 */
@Serializable
data class FullProfile(
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val email: String,
    @SerialName("profile_image")
    val profileImage: String? = null,
    val bio: String = "",
    val title: String = "",
    val company: String = "",
    val location: String = "",
    val industry: String = "",
    val skills: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    @SerialName("is_mentor")
    val isMentor: Boolean = false,
    @SerialName("is_entrepreneur")
    val isEntrepreneur: Boolean = false,
    @SerialName("is_verified")
    val isVerified: Boolean = false,
    @SerialName("connection_status")
    val connectionStatus: String = "none", // none, pending, connected
    @SerialName("mutual_connections")
    val mutualConnections: Int = 0
)

/**
 * Connection Request Result
 */
data class ConnectionRequestResult(
    val success: Boolean,
    val message: String
)

