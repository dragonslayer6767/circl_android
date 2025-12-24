package com.fragne.circl_app.tutorial

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.fragne.circl_app.tutorial.content.TutorialContentFactory
import com.fragne.circl_app.tutorial.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Tutorial Manager
 * Central state management for tutorial system
 * Singleton pattern for global access
 */
class TutorialManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    private val contentFactory = TutorialContentFactory()

    // StateFlow for reactive state management
    private val _currentFlow = MutableStateFlow<TutorialFlow?>(null)
    val currentFlow: StateFlow<TutorialFlow?> = _currentFlow.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _isShowingTutorial = MutableStateFlow(false)
    val isShowingTutorial: StateFlow<Boolean> = _isShowingTutorial.asStateFlow()

    private val _tutorialState = MutableStateFlow<TutorialState>(TutorialState.NotStarted)
    val tutorialState: StateFlow<TutorialState> = _tutorialState.asStateFlow()

    private val _userType = MutableStateFlow(UserType.COMMUNITY_BUILDER)
    val userType: StateFlow<UserType> = _userType.asStateFlow()

    // Track which tutorial type is currently running (may differ from userType)
    private var currentTutorialType: UserType = UserType.COMMUNITY_BUILDER

    // Prevent multiple simultaneous tutorial starts
    private var isTutorialStarting: Boolean = false

    // Navigation callback for tutorial navigation
    private var navigationCallback: ((String) -> Unit)? = null

    init {
        loadUserType()
        loadTutorialProgress()
    }

    companion object {
        private const val TAG = "TutorialManager"
        private const val PREF_NAME = "tutorial_preferences"
        private const val KEY_USER_TYPE = "user_type_detected"
        private const val KEY_TUTORIAL_COMPLETED = "tutorial_completed_"
        private const val KEY_CURRENT_STEP = "tutorial_current_step"
        private const val KEY_CURRENT_FLOW = "tutorial_current_flow"
        private const val KEY_JUST_COMPLETED_ONBOARDING = "just_completed_onboarding"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

        @Volatile
        private var INSTANCE: TutorialManager? = null

        fun getInstance(context: Context): TutorialManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TutorialManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // MARK: - User Type Management

    /**
     * Set the user type manually
     */
    fun setUserType(type: UserType) {
        _userType.value = type
        sharedPreferences.edit().putString(KEY_USER_TYPE, type.name).apply()
        Log.d(TAG, "User type set to: ${type.displayName}")
    }

    /**
     * Detect and set user type from onboarding data
     */
    fun detectAndSetUserType(onboardingData: OnboardingData) {
        val detectedType = UserType.detectUserType(onboardingData)
        setUserType(detectedType)
    }

    /**
     * Load saved user type from preferences
     */
    private fun loadUserType() {
        val typeString = sharedPreferences.getString(KEY_USER_TYPE, null)
        if (typeString != null) {
            try {
                _userType.value = UserType.valueOf(typeString)
                Log.d(TAG, "Loaded user type: ${_userType.value.displayName}")
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid user type: $typeString", e)
            }
        }
    }

    // MARK: - Tutorial Flow Management

    /**
     * Start tutorial for a specific user type
     * If userType is null, uses the detected user type
     */
    fun startTutorial(userType: UserType? = null) {
        if (isTutorialStarting) {
            Log.w(TAG, "Tutorial is already starting, ignoring duplicate call")
            return
        }

        isTutorialStarting = true

        try {
            val targetUserType = userType ?: _userType.value

            // Don't start if already completed (unless this is a manual restart)
            if (hasTutorialBeenCompleted(targetUserType) && userType == null) {
                Log.d(TAG, "Tutorial already completed for user type: ${targetUserType.displayName}")
                isTutorialStarting = false
                return
            }

            val flow = getTutorialFlow(targetUserType)
            if (flow == null) {
                Log.e(TAG, "No tutorial flow found for user type: ${targetUserType.displayName}")
                isTutorialStarting = false
                return
            }

            // Reset tutorial state
            _currentFlow.value = flow
            _currentStepIndex.value = 0
            currentTutorialType = targetUserType
            _tutorialState.value = TutorialState.InProgress(0)
            _isShowingTutorial.value = true

            Log.d(TAG, "🎓 Started tutorial for ${targetUserType.displayName}: ${flow.title}")

            // Handle navigation for first step if needed
            handleStepNavigation()
        } finally {
            isTutorialStarting = false
        }
    }

    /**
     * Move to the next tutorial step
     */
    fun nextStep() {
        val flow = _currentFlow.value ?: return
        val currentIndex = _currentStepIndex.value

        if (currentIndex < flow.steps.size - 1) {
            _currentStepIndex.value = currentIndex + 1
            _tutorialState.value = TutorialState.InProgress(_currentStepIndex.value)
            saveTutorialProgress()

            Log.d(TAG, "⏭️ Advanced to step ${_currentStepIndex.value + 1}/${flow.steps.size}")

            // Handle navigation if the new step requires it
            handleStepNavigation()
        } else {
            completeTutorial()
        }
    }

    /**
     * Move to the previous tutorial step
     */
    fun previousStep() {
        val currentIndex = _currentStepIndex.value

        if (currentIndex > 0) {
            _currentStepIndex.value = currentIndex - 1
            _tutorialState.value = TutorialState.InProgress(_currentStepIndex.value)
            saveTutorialProgress()

            Log.d(TAG, "⏮️ Moved back to step ${_currentStepIndex.value + 1}")

            // Handle navigation for previous step
            handleStepNavigation()
        }
    }

    /**
     * Skip the tutorial
     */
    fun skipTutorial() {
        Log.d(TAG, "⏭️ Tutorial skipped by user")
        _tutorialState.value = TutorialState.Skipped
        _isShowingTutorial.value = false
        _currentFlow.value = null
        _currentStepIndex.value = 0

        // Clear just_completed_onboarding flag
        sharedPreferences.edit().remove(KEY_JUST_COMPLETED_ONBOARDING).apply()
    }

    /**
     * Complete the tutorial
     */
    fun completeTutorial() {
        val flow = _currentFlow.value
        if (flow != null) {
            Log.d(TAG, "✅ Tutorial completed: ${flow.title}")
            markTutorialCompleted(flow.userType)
        }

        _tutorialState.value = TutorialState.Completed
        _isShowingTutorial.value = false
        _currentFlow.value = null
        _currentStepIndex.value = 0

        // Clear just_completed_onboarding flag
        sharedPreferences.edit().remove(KEY_JUST_COMPLETED_ONBOARDING).apply()
    }

    /**
     * Restart tutorial for a specific user type
     */
    fun restartTutorial(userType: UserType = _userType.value) {
        Log.d(TAG, "🔄 Restarting tutorial for: ${userType.displayName}")

        // Clear completion status
        sharedPreferences.edit()
            .remove("$KEY_TUTORIAL_COMPLETED${userType.name}")
            .apply()

        // Start tutorial
        startTutorial(userType)
    }

    /**
     * Check if tutorial should be triggered and start it
     * Called after app launch or login
     */
    fun checkAndTriggerTutorial() {
        val justCompletedOnboarding = sharedPreferences.getBoolean(KEY_JUST_COMPLETED_ONBOARDING, false)
        val onboardingCompleted = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        val tutorialCompleted = hasTutorialBeenCompleted(_userType.value)

        Log.d(TAG, "🔍 Check tutorial trigger - " +
                "justCompletedOnboarding=$justCompletedOnboarding, " +
                "onboardingCompleted=$onboardingCompleted, " +
                "tutorialCompleted=$tutorialCompleted")

        if (justCompletedOnboarding && !tutorialCompleted) {
            Log.d(TAG, "🎯 Triggering tutorial automatically")
            startTutorial()
        }
    }

    /**
     * Get the current tutorial step
     */
    val currentStep: TutorialStep?
        get() {
            val flow = _currentFlow.value ?: return null
            val index = _currentStepIndex.value
            return if (index in flow.steps.indices) flow.steps[index] else null
        }

    // MARK: - Tutorial Flow Retrieval

    /**
     * Get tutorial flow for a specific user type
     */
    private fun getTutorialFlow(userType: UserType): TutorialFlow? {
        return when (userType) {
            UserType.ENTREPRENEUR -> contentFactory.createEntrepreneurTutorial()
            UserType.STUDENT -> contentFactory.createStudentTutorial()
            UserType.STUDENT_ENTREPRENEUR -> contentFactory.createStudentEntrepreneurTutorial()
            UserType.MENTOR -> contentFactory.createMentorTutorial()
            UserType.INVESTOR -> contentFactory.createInvestorTutorial()
            UserType.COMMUNITY_BUILDER -> contentFactory.createCommunityBuilderTutorial()
            UserType.OTHER -> contentFactory.createCommunityBuilderTutorial()
        }
    }

    // MARK: - Persistence

    /**
     * Save current tutorial progress
     */
    private fun saveTutorialProgress() {
        sharedPreferences.edit()
            .putInt(KEY_CURRENT_STEP, _currentStepIndex.value)
            .putString(KEY_CURRENT_FLOW, _currentFlow.value?.userType?.name)
            .apply()
    }

    /**
     * Load saved tutorial progress
     */
    private fun loadTutorialProgress() {
        val stepIndex = sharedPreferences.getInt(KEY_CURRENT_STEP, 0)
        val flowType = sharedPreferences.getString(KEY_CURRENT_FLOW, null)

        if (flowType != null) {
            try {
                val type = UserType.valueOf(flowType)
                // Only restore if not completed
                if (!hasTutorialBeenCompleted(type)) {
                    _currentStepIndex.value = stepIndex
                    Log.d(TAG, "Loaded tutorial progress: step $stepIndex for $flowType")
                }
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Invalid flow type: $flowType", e)
            }
        }
    }

    /**
     * Check if tutorial has been completed for a user type
     */
    fun hasTutorialBeenCompleted(userType: UserType): Boolean {
        return sharedPreferences.getBoolean("$KEY_TUTORIAL_COMPLETED${userType.name}", false)
    }

    /**
     * Mark tutorial as completed for a user type
     */
    private fun markTutorialCompleted(userType: UserType) {
        sharedPreferences.edit()
            .putBoolean("$KEY_TUTORIAL_COMPLETED${userType.name}", true)
            .remove(KEY_JUST_COMPLETED_ONBOARDING)
            .apply()

        Log.d(TAG, "✅ Marked tutorial as completed for: ${userType.displayName}")
    }

    // MARK: - Navigation Handling

    /**
     * Set navigation callback for tutorial navigation
     */
    fun setNavigationCallback(callback: (String) -> Unit) {
        navigationCallback = callback
        Log.d(TAG, "Navigation callback set")
    }

    /**
     * Handle navigation for current step
     * This will be implemented to work with your NavigationManager
     */
    private fun handleStepNavigation() {
        val step = currentStep ?: return
        val destination = step.navigationDestination ?: return

        Log.d(TAG, "🧭 Navigating to: $destination for step: ${step.title}")

        // Call the navigation callback if set
        navigationCallback?.invoke(destination)
    }

    // MARK: - Debug & Testing

    /**
     * Reset all tutorial state (for testing)
     */
    fun resetAllTutorialState() {
        sharedPreferences.edit().clear().apply()
        _currentFlow.value = null
        _currentStepIndex.value = 0
        _isShowingTutorial.value = false
        _tutorialState.value = TutorialState.NotStarted
        _userType.value = UserType.COMMUNITY_BUILDER

        Log.d(TAG, "🔄 Reset all tutorial state")
    }
}

