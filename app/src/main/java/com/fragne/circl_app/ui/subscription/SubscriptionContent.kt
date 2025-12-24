package com.fragne.circl_app.ui.subscription

/**
 * Subscription Content Factory
 * Creates subscription content for different user types
 * Translated from SubscriptionContent.swift
 *
 * NOTE: All user types now use universal subscription plans (Student+, Entrepreneur+, FounderX)
 * Only the background image and titles differ per user type
 */
object SubscriptionContentFactory {

    fun createSubscriptionContent(userType: UserType): SubscriptionContent {
        return when (userType) {
            UserType.ENTREPRENEUR -> createEntrepreneurSubscription()
            UserType.STUDENT -> createStudentSubscription()
            UserType.STUDENT_ENTREPRENEUR -> createStudentEntrepreneurSubscription()
            UserType.MENTOR -> createMentorSubscription()
            UserType.COMMUNITY_BUILDER -> createCommunityBuilderSubscription()
            UserType.INVESTOR -> createInvestorSubscription()
            UserType.OTHER -> {
                println("⚠️ WARNING: OTHER user type detected in subscription flow - this should not happen")
                println("🔧 Check detectUserType() logic")
                createCommunityBuilderSubscription() // Safe fallback
            }
        }
    }

    // MARK: - Universal Plans (used by all user types)
    private fun universalPlans(): List<SubscriptionPlan> {
        return listOf(
            SubscriptionPlan(
                title = "Student+",
                price = "$7.99",
                period = "monthly",
                features = listOf(
                    "Unlimited daily connections (vs 4/day free limit), mentor matches, and Circle creation",
                    "Earn more with 50% less transaction fee at 7% compared to 14% transaction fee",
                    "Full circles dashboard access: Task Manager, KPI Tracking, Calendar and Events feature",
                    "2 free marketplace boosts (30% more visibility, \$15 value each)",
                    "Priority Access to future products such as CRM integration and Video call features",
                    "* Must validate student email via profile to qualify for Student+ pricing"
                )
            ),
            SubscriptionPlan(
                title = "Entrepreneur+",
                price = "$29.99",
                period = "monthly",
                features = listOf(
                    "Unlimited daily connections (vs 4/day free limit), mentor matches, and Circle creation",
                    "Earn more with 50% less transaction fee at 7% compared to 14% transaction fee",
                    "Full circles dashboard access: Task Manager, KPI Tracking, Calendar and Events feature",
                    "Unlimited job and project listings and monetization (vs 2/month free limit)",
                    "Advanced circle dashboard: Real-time analytics, CRM import/API integration",
                    "2 free marketplace boosts + 1 monthly recurring boost (45% more visibility, \$45 value)",
                    "Priority Access to future products such as CRM integration and Video call features",
                    "Advanced search filters: Industry, location, experience level, funding stage"
                ),
                isPopular = true
            ),
            SubscriptionPlan(
                title = "FounderX",
                price = "$54.99",
                period = "monthly",
                features = listOf(
                    "Unlimited daily connections (vs 4/day free limit), mentor matches, and Circle creation",
                    "Earn more with 50% less transaction fee at 7% compared to 14% transaction fee",
                    "Full circles dashboard access: Task Manager, KPI Tracking, Calendar and Events feature",
                    "Unlimited job and project listings and monetization (vs 2/month free limit)",
                    "Advanced circle dashboard: Real-time analytics, CRM import/API integration",
                    "2 free marketplace boosts + 2 monthly recurring boosts (60% more visibility, \$60 value)",
                    "Early Access to new products including CRM integration and Video call features",
                    "Advanced search filters: Industry, location, experience level, funding stage",
                    "Priority support with 24-hour response guarantee",
                    "Access to 500+ verified investors and exclusive networking events"
                )
            )
        )
    }

    // MARK: - Entrepreneur Subscription (uses universal plans)
    private fun createEntrepreneurSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.ENTREPRENEUR,
            backgroundImage = "entrepreneur",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }

    // MARK: - Student Subscription (uses universal plans)
    private fun createStudentSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.STUDENT,
            backgroundImage = "student",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }

    // MARK: - Student Entrepreneur Subscription (uses universal plans)
    private fun createStudentEntrepreneurSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.STUDENT_ENTREPRENEUR,
            backgroundImage = "student_entrepreneur",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }

    // MARK: - Mentor Subscription (uses universal plans)
    private fun createMentorSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.MENTOR,
            backgroundImage = "mentor",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }

    // MARK: - Community Builder Subscription (uses universal plans)
    private fun createCommunityBuilderSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.COMMUNITY_BUILDER,
            backgroundImage = "community_builder",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }

    // MARK: - Investor Subscription (uses universal plans)
    private fun createInvestorSubscription(): SubscriptionContent {
        return SubscriptionContent(
            userType = UserType.INVESTOR,
            backgroundImage = "investor",
            title = "Take Control Of Your Future",
            subtitle = "Circl is designed to be your success command center",
            benefits = emptyList(),
            plans = universalPlans()
        )
    }
}

