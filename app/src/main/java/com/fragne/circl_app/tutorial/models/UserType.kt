package com.fragne.circl_app.tutorial.models

import android.util.Log

/**
 * User Type Detection
 * Maps onboarding responses to tutorial types
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
        private const val TAG = "UserType"

        /**
         * Detect user type from onboarding data
         * Maps Page3 "Main Usage Interests" to appropriate tutorial flow
         */
        fun detectUserType(onboardingData: OnboardingData): UserType {
            val interests = onboardingData.usageInterests.lowercase()
            val industry = onboardingData.industryInterests.lowercase()

            Log.d(TAG, "🔍 Detecting user type from interests: '$interests' and industry: '$industry'")
            Log.d(TAG, "📋 Available Page3 options: Student, Start Your Business, Scale Your Business, " +
                    "Network with Entrepreneurs, Find Co-Founder/s, Find Mentors, Find Investors, " +
                    "Be Part of the Community, Share Knowledge, Make Investments, Sell a Skill")

            // EXACT MAPPING from Page3 "Main Usage Interests":

            // Student --> Student Tutorial (only if no business interests)
            if (interests.contains("student") &&
                !interests.contains("entrepreneur") &&
                !interests.contains("start your business") &&
                !interests.contains("scale your business")) {
                Log.d(TAG, "✅ Detected: Student (from 'Student' interest)")
                return STUDENT
            }

            // Check for student entrepreneur (student + business interests)
            if (interests.contains("student") &&
                (interests.contains("entrepreneur") ||
                interests.contains("start your business") ||
                interests.contains("scale your business"))) {
                Log.d(TAG, "✅ Detected: Student Entrepreneur")
                return STUDENT_ENTREPRENEUR
            }

            // Start Your Business --> Entrepreneur Tutorial
            if (interests.contains("start your business")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Start Your Business' interest)")
                return ENTREPRENEUR
            }

            // Scale Your Business --> Entrepreneur Tutorial
            if (interests.contains("scale your business")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Scale Your Business' interest)")
                return ENTREPRENEUR
            }

            // Network with Entrepreneurs --> Entrepreneur Tutorial
            if (interests.contains("network with entrepreneurs")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Network with Entrepreneurs' interest)")
                return ENTREPRENEUR
            }

            // Find Co-Founder/s --> Entrepreneur Tutorial
            if (interests.contains("find co-founder")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Find Co-Founder/s' interest)")
                return ENTREPRENEUR
            }

            // Find Mentors --> Entrepreneur Tutorial
            if (interests.contains("find mentors")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Find Mentors' interest)")
                return ENTREPRENEUR
            }

            // Find Investors --> Entrepreneur Tutorial
            if (interests.contains("find investors")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Find Investors' interest)")
                return ENTREPRENEUR
            }

            // Make Investments --> Investor
            if (interests.contains("make investments")) {
                Log.d(TAG, "✅ Detected: Investor (from 'Make Investments' interest)")
                return INVESTOR
            }

            // Share Knowledge --> Mentor
            if (interests.contains("share knowledge")) {
                Log.d(TAG, "✅ Detected: Mentor (from 'Share Knowledge' interest)")
                return MENTOR
            }

            // Sell a Skill --> Entrepreneur Tutorial
            if (interests.contains("sell a skill")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from 'Sell a Skill' interest)")
                return ENTREPRENEUR
            }

            // Be Part of the Community --> Community Builder
            if (interests.contains("be part of the community")) {
                Log.d(TAG, "✅ Detected: Community Builder (from 'Be Part of the Community' interest)")
                return COMMUNITY_BUILDER
            }

            // Fallback checks for broader keyword matching
            if (interests.contains("entrepreneur") || industry.contains("startups & entrepreneurship")) {
                Log.d(TAG, "✅ Detected: Entrepreneur (from broad keyword matching)")
                return ENTREPRENEUR
            }

            if (interests.contains("invest") || interests.contains("investor") || interests.contains("funding")) {
                Log.d(TAG, "✅ Detected: Investor (from broad keyword matching)")
                return INVESTOR
            }

            if (interests.contains("mentor") || interests.contains("teaching")) {
                Log.d(TAG, "✅ Detected: Mentor (from broad keyword matching)")
                return MENTOR
            }

            if (interests.contains("community") || interests.contains("networking")) {
                Log.d(TAG, "✅ Detected: Community Builder (from broad keyword matching)")
                return COMMUNITY_BUILDER
            }

            // Default to community builder for general users
            Log.d(TAG, "✅ Detected: Community Builder (default)")
            return COMMUNITY_BUILDER
        }
    }
}

