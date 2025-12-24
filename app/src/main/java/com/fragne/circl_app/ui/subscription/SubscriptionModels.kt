package com.fragne.circl_app.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Subscription Plan Model
 * Represents a single subscription tier with features and pricing
 */
data class SubscriptionPlan(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val price: String,
    val period: String, // "monthly", "yearly"
    val features: List<String>,
    val isPopular: Boolean = false,
    val originalPrice: String? = null,
    val discount: String? = null
)

/**
 * Subscription Content Model
 * Contains all content for a specific user type's paywall
 */
data class SubscriptionContent(
    val userType: UserType,
    val backgroundImage: String,
    val title: String,
    val subtitle: String,
    val benefits: List<String>,
    val plans: List<SubscriptionPlan>
)

/**
 * User Type Enum
 * Defines different user categories with specific subscription offerings
 */
enum class UserType(val displayName: String) {
    ENTREPRENEUR("Entrepreneur"),
    STUDENT("Student"),
    STUDENT_ENTREPRENEUR("Student Entrepreneur"),
    MENTOR("Mentor"),
    COMMUNITY_BUILDER("Community Builder"),
    INVESTOR("Investor"),
    OTHER("Other");

    companion object {
        fun detectUserType(usageInterests: String, industryInterests: String): UserType {
            // Simple detection logic - can be enhanced based on your onboarding flow
            return when {
                usageInterests.contains("entrepreneur", ignoreCase = true) -> ENTREPRENEUR
                usageInterests.contains("student", ignoreCase = true) &&
                    usageInterests.contains("business", ignoreCase = true) -> STUDENT_ENTREPRENEUR
                usageInterests.contains("student", ignoreCase = true) -> STUDENT
                usageInterests.contains("mentor", ignoreCase = true) -> MENTOR
                usageInterests.contains("community", ignoreCase = true) -> COMMUNITY_BUILDER
                usageInterests.contains("invest", ignoreCase = true) -> INVESTOR
                else -> ENTREPRENEUR // Default fallback
            }
        }
    }
}

/**
 * Subscription State
 * Tracks the current state of the paywall presentation
 */
enum class SubscriptionState {
    NOT_SHOWING,
    SHOWING_BACKGROUND,
    SHOWING_CONTENT,
    COMPLETED,
    DISMISSED
}

/**
 * Subscription Manager ViewModel
 * Manages subscription paywall state and user interactions
 */
class SubscriptionManager : ViewModel() {

    // State flows
    private val _isShowingPaywall = MutableStateFlow(false)
    val isShowingPaywall: StateFlow<Boolean> = _isShowingPaywall.asStateFlow()

    private val _subscriptionState = MutableStateFlow(SubscriptionState.NOT_SHOWING)
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState.asStateFlow()

    private val _currentContent = MutableStateFlow<SubscriptionContent?>(null)
    val currentContent: StateFlow<SubscriptionContent?> = _currentContent.asStateFlow()

    private val _selectedPlan = MutableStateFlow<SubscriptionPlan?>(null)
    val selectedPlan: StateFlow<SubscriptionPlan?> = _selectedPlan.asStateFlow()

    companion object {
        private const val PREF_HAS_SEEN_PAYWALL = "has_seen_paywall"

        // Singleton instance
        @Volatile
        private var instance: SubscriptionManager? = null

        fun getInstance(): SubscriptionManager {
            return instance ?: synchronized(this) {
                instance ?: SubscriptionManager().also { instance = it }
            }
        }
    }

    /**
     * Show paywall for specific user type
     */
    fun showPaywall(userType: UserType) {
        println("🎯 PAYWALL SHOW: Starting for ${userType.displayName}")
        println("🎯 PAYWALL SHOW: Current state before: ${_subscriptionState.value}")
        println("🎯 PAYWALL SHOW: isShowingPaywall before: ${_isShowingPaywall.value}")

        viewModelScope.launch {
            if (_isShowingPaywall.value) {
                // Paywall already showing, just update content
                println("🎯 PAYWALL SHOW: Paywall already showing, just updating content")
                _subscriptionState.value = SubscriptionState.NOT_SHOWING
                _currentContent.value = null
                _selectedPlan.value = null

                // Get new content
                _currentContent.value = createSubscriptionContent(userType)

                // Restart sequence
                _subscriptionState.value = SubscriptionState.SHOWING_BACKGROUND

                // Show content after delay
                delay(600)
                _subscriptionState.value = SubscriptionState.SHOWING_CONTENT
                println("🎯 PAYWALL SHOW: Set to showingContent (update)")
            } else {
                // Fresh paywall presentation
                println("🎯 PAYWALL SHOW: Fresh presentation")

                // Reset state
                _subscriptionState.value = SubscriptionState.NOT_SHOWING
                _currentContent.value = null
                _selectedPlan.value = null

                println("🎯 PAYWALL SHOW: State reset to NOT_SHOWING")

                // Get content for user type
                _currentContent.value = createSubscriptionContent(userType)

                // Show paywall with background
                _subscriptionState.value = SubscriptionState.SHOWING_BACKGROUND
                _isShowingPaywall.value = true

                println("🎯 PAYWALL SHOW: Set to SHOWING_BACKGROUND, isShowingPaywall = true")

                // Show content after 600ms delay
                delay(600)
                _subscriptionState.value = SubscriptionState.SHOWING_CONTENT
                println("🎯 PAYWALL SHOW: Set to SHOWING_CONTENT (fresh)")
            }
        }
    }

    /**
     * Dismiss paywall
     */
    fun dismissPaywall() {
        println("🎯 PAYWALL DISMISS: Starting dismissal")
        println("🎯 PAYWALL DISMISS: Current state before: ${_subscriptionState.value}")

        _subscriptionState.value = SubscriptionState.NOT_SHOWING
        _isShowingPaywall.value = false
        _currentContent.value = null
        _selectedPlan.value = null

        println("🎯 PAYWALL DISMISS: Completed, isShowingPaywall = false")
    }

    /**
     * Complete subscription purchase
     */
    fun completeSubscription() {
        _subscriptionState.value = SubscriptionState.COMPLETED
        _isShowingPaywall.value = false

        // Handle subscription completion logic here
        println("✅ Subscription completed for plan: ${_selectedPlan.value?.title ?: "Unknown"}")

        // TODO: Integrate with actual payment system
    }

    /**
     * Select a subscription plan
     */
    fun selectPlan(plan: SubscriptionPlan) {
        _selectedPlan.value = plan
    }

    /**
     * Create subscription content for user type
     */
    private fun createSubscriptionContent(userType: UserType): SubscriptionContent {
        return SubscriptionContentFactory.createSubscriptionContent(userType)
    }

    /**
     * Check if user has seen paywall before
     */
    fun hasSeenPaywall(): Boolean {
        // TODO: Implement with SharedPreferences
        return false
    }

    /**
     * Reset paywall status (for testing)
     */
    fun resetPaywallStatus() {
        println("🔄 Paywall status reset")
        // TODO: Clear SharedPreferences
    }
}

