package com.fragne.circl_app.core.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// MARK: - Data Models

/**
 * Model for mentor profile data from the mentors API
 */
@Serializable
data class MentorProfileData(
    @SerialName("user_id") val userId: Int,
    val name: String,
    val username: String,
    val title: String,
    val company: String,
    val proficiency: String,
    val tags: List<String>,
    val email: String,
    @SerialName("profileImage") val profileImage: String? = null
)

/**
 * Model for invite/friend request profile data
 */
@Serializable
data class InviteProfileData(
    @SerialName("user_id") val userId: Int,
    val name: String,
    val username: String,
    val email: String,
    val title: String,
    val company: String,
    val proficiency: String,
    val tags: List<String>,
    @SerialName("profileImage") val profileImage: String? = null
)

/**
 * Model for entrepreneur profile data from the entrepreneurs API
 */
@Serializable
data class EntrepreneurProfileData(
    @SerialName("user_id") val userId: Int,
    val name: String,
    val email: String,
    val username: String,
    @SerialName("profileImage") val profileImage: String? = null,
    val businessName: String,
    val businessStage: String,
    val businessIndustry: String,
    val businessBio: String,
    val fundingRaised: String,
    val lookingFor: List<String>,
    val isMentor: Boolean
) {
    // Computed properties for compatibility with MentorProfileData
    val title: String get() = businessStage
    val company: String get() = businessName
    val proficiency: String get() = businessIndustry
    val tags: List<String> get() = lookingFor
}

// MARK: - Response Models

/**
 * API response model for network/friends data
 */
@Serializable
data class NetworkResponse(
    val success: Boolean,
    val users: List<InviteProfileData>
)

/**
 * API response model for invite data
 */
@Serializable
data class InviteResponse(
    val success: Boolean,
    val users: List<InviteProfileData>
)

/**
 * API response model for entrepreneurs data
 */
@Serializable
data class EntrepreneurResponse(
    val success: Boolean,
    val entrepreneurs: List<EntrepreneurProfileData>
)

/**
 * API response model for mentors data
 */
@Serializable
data class MentorResponse(
    val success: Boolean,
    val mentors: List<MentorProfileData>
)

// MARK: - User Model

/**
 * Current user profile data
 */
@Serializable
data class UserProfile(
    @SerialName("user_id") val userId: Int,
    val name: String,
    val email: String,
    val username: String,
    @SerialName("profileImage") val profileImage: String? = null,
    val title: String? = null,
    val company: String? = null,
    val proficiency: String? = null,
    val tags: List<String> = emptyList(),
    val isMentor: Boolean = false,
    val isEntrepreneur: Boolean = false
)

