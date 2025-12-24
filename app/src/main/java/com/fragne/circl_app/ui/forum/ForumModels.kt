package com.fragne.circl_app.ui.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Forum Post Model
 * Represents a single post in the forum feed
 */
@Serializable
data class ForumPostModel(
    val id: Int,
    val user: String,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("profile_image")
    val profileImage: String? = null,
    val content: String,
    val category: String,
    val privacy: String,
    val image: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("comment_count")
    val commentCount: Int = 0,
    @SerialName("like_count")
    val likeCount: Int = 0,
    @SerialName("liked_by_user")
    val likedByUser: Boolean = false,
    @SerialName("is_mentor")
    val isMentor: Boolean = false
)

/**
 * Comment Model
 * Represents a comment on a forum post
 */
@Serializable
data class CommentModel(
    val id: Int,
    val user: String,
    val text: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("like_count")
    val likeCount: Int = 0,
    @SerialName("liked_by_user")
    val likedByUser: Boolean = false,
    @SerialName("profile_image")
    val profileImage: String? = null
)

/**
 * Forum UI State
 */
data class ForumUiState(
    val posts: List<ForumPostModel> = emptyList(),
    val isLoading: Boolean = false,
    val isTabSwitchLoading: Boolean = false,
    val selectedFilter: String = "public",
    val selectedCategory: String = "Category",
    val selectedPrivacy: String = "Public",
    val postContent: String = "",
    val userFirstName: String = "",
    val userProfileImageUrl: String = "",
    val unreadMessageCount: Int = 0,
    val errorMessage: String? = null
)

/**
 * Report Reason enum
 */
enum class ReportReason(val displayName: String) {
    SPAM("Spam or misleading"),
    HARASSMENT("Harassment or hate speech"),
    EXPLICIT("Explicit content"),
    VIOLENCE("Violence or harmful behavior"),
    OTHER("Other")
}

