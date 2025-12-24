package com.fragne.circl_app.ui.circles.calendar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Circle Data Models
 * Translated from CircleDataModels.swift
 */

// MARK: - Message Model
@Serializable
data class MessageModel(
    val id: Int,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("receiver_id") val receiverId: Int,
    val content: String,
    val timestamp: String,
    @SerialName("is_read") val isRead: Boolean,
    val mediaURL: String? = null
)

// MARK: - Channel Model
@Serializable
data class ChannelModel(
    val id: Int,
    val name: String,
    @SerialName("circle_id") val circleId: Int,
    val position: Int = 0,
    @SerialName("is_moderator_only") val isModeratorOnly: Boolean = false
)

// MARK: - Channel Category Response
@Serializable
data class ChannelCategoryResponse(
    val id: Int? = null,  // null for Uncategorized
    val name: String,
    val position: Int,
    val channels: List<ChannelModel>
)

// MARK: - Channel Category
@Serializable
data class ChannelCategoryModel(
    val id: Int?,
    val name: String,
    val position: Int,
    val channels: List<ChannelModel>
)

// MARK: - Join Type Enum
enum class JoinType(val displayName: String) {
    APPLY_NOW("Apply Now"),
    JOIN_NOW("Join Now"),
    REQUEST_TO_JOIN("Request to Join");

    companion object {
        fun fromString(value: String): JoinType {
            return when (value.lowercase()) {
                "apply_now", "apply now" -> APPLY_NOW
                "request_to_join", "request to join" -> REQUEST_TO_JOIN
                else -> JOIN_NOW
            }
        }
    }
}

// MARK: - Circle Data (Main Model)
@Serializable
data class CircleDataModel(
    val id: Int,
    val name: String,
    val industry: String,
    @SerialName("member_count") val memberCount: Int,
    @SerialName("image_name") val imageName: String? = null,
    val pricing: String,
    val description: String,
    @SerialName("join_type") val joinTypeString: String,
    val channels: List<String>,
    @SerialName("creator_id") val creatorId: Int,
    @SerialName("is_moderator") val isModerator: Boolean,
    @SerialName("is_private") val isPrivate: Boolean,
    @SerialName("has_dashboard") val hasDashboard: Boolean? = false,
    @SerialName("is_dashboard_public") val isDashboardPublic: Boolean? = false,
    @SerialName("dues_amount") val duesAmount: Int? = null,
    @SerialName("has_stripe_account") val hasStripeAccount: Boolean? = null,
    @SerialName("access_code") val accessCode: String? = null,
    @SerialName("profile_image_url") val profileImageURL: String? = null
) {
    // Computed property for JoinType enum
    val joinType: JoinType
        get() = JoinType.fromString(joinTypeString)
}

// MARK: - Category With Channels (UI Model)
data class CategoryWithChannels(
    val id: Int?,  // nil for "Uncategorized"
    val name: String,
    val position: Int,
    val channels: MutableList<ChannelModel>
)

