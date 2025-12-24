package com.fragne.circl_app.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation routes using type-safe navigation with Kotlin Serialization
 */
sealed interface Route {

    // Auth routes
    @Serializable
    data object Page1 : Route  // Login/Entry

    @Serializable
    data object Page17 : Route  // Ethics

    @Serializable
    data object Page14 : Route  // Terms & Conditions

    @Serializable
    data object Page3 : Route  // User Info Form

    @Serializable
    data object Page4 : Route  // Profile Picture

    @Serializable
    data object Page5 : Route  // Personal Info

    @Serializable
    data object Page13 : Route  // Notifications

    @Serializable
    data object Page19 : Route  // Welcome/Completion

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Signup : Route

    // Main app routes
    @Serializable
    data object Network : Route

    @Serializable
    data object Circles : Route

    @Serializable
    data class CircleGroupChat(
        val circleId: Int,
        val circleName: String
    ) : Route

    @Serializable
    data class CircleChannelMessages(
        val channelId: Int,
        val channelName: String,
        val circleName: String
    ) : Route

    @Serializable
    data class ManageChannels(
        val circleId: Int,
        val circleName: String
    ) : Route

    @Serializable
    data class MemberList(
        val circleId: Int,
        val circleName: String
    ) : Route

    @Serializable
    data class DashboardMemberList(
        val circleId: Int,
        val circleName: String
    ) : Route

    @Serializable
    data class CircleDues(
        val circleId: Int,
        val circleName: String
    ) : Route

    @Serializable
    data object Forum : Route

    @Serializable
    data object More : Route

    // Messages routes
    @Serializable
    data object Messages : Route

    @Serializable
    data class Chat(
        val userId: String,
        val userName: String,
        val userEmail: String
    ) : Route

    // Profile routes
    @Serializable
    data class Profile(val userId: Int) : Route

    @Serializable
    data class ProfilePreview(val userId: Int) : Route

    @Serializable
    data object MyProfile : Route

    @Serializable
    data object EditProfile : Route

    // Business routes
    @Serializable
    data class BusinessProfile(val businessId: Int) : Route

    @Serializable
    data object EditBusinessProfile : Route

    // Settings routes
    @Serializable
    data object Settings : Route

    @Serializable
    data object BecomeMentor : Route

    @Serializable
    data object ChangePassword : Route

    @Serializable
    data object BlockedUsers : Route

    @Serializable
    data object DeleteAccount : Route

    @Serializable
    data object SuggestFeature : Route

    @Serializable
    data object ReportProblem : Route

    @Serializable
    data object TermsOfService : Route

    @Serializable
    data object PrivacyPolicy : Route

    @Serializable
    data object CommunityGuidelines : Route

    @Serializable
    data object ContactSupport : Route

    @Serializable
    data object Notifications : Route

    @Serializable
    data object Subscription : Route

    @Serializable
    data object Tutorial : Route
}

