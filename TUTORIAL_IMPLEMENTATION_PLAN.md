# Tutorial System Implementation Plan - Android (Jetpack Compose)
## Overview
This document outlines the comprehensive implementation of a personalized tutorial system for the Circl Android app, based on the iOS Swift implementation.
## Implementation Status: Phase 1 Complete ✅
### ✅ Phase 1: Core Models & Manager (COMPLETED)
#### Models Created (`tutorial/models/`):
1. ✅ **UserType.kt** - Enum with detection logic
2. ✅ **OnboardingData.kt** - Onboarding data capture
3. ✅ **TutorialStep.kt** - Individual step model
4. ✅ **TutorialFlow.kt** - Complete flow model  
5. ✅ **TutorialProgress.kt** - Progress tracking
6. ✅ **TutorialState.kt** - State management
#### Manager Created:
7. ✅ **TutorialManager.kt** - Central controller with StateFlow
#### Content Factory Created (`tutorial/content/`):
8. ✅ **TutorialContentFactory.kt** - All 6 user type tutorials
   - Entrepreneur Tutorial (12 steps)
   - Student Tutorial (8 steps)
   - Student Entrepreneur Tutorial (8 steps)
   - Mentor Tutorial (8 steps)
   - Investor Tutorial (8 steps)
   - Community Builder Tutorial (8 steps)
#### UI Components Created (`tutorial/ui/`):
9. ✅ **TutorialOverlay.kt** - Main overlay system
10. ✅ **TutorialTooltip.kt** - Step tooltip component
11. ✅ **HighlightCutout.kt** - Highlight & progress components
12. ✅ **CommunityWelcomeOverlay.kt** - Final welcome screen
## Next Steps: Integration Phase
### Phase 2: Onboarding Integration
- [ ] Update Page3Screen.kt to detect user type
- [ ] Set onboarding completion flags
- [ ] Test user type detection
### Phase 3: Main App Integration
- [ ] Update RootNavigation.kt to trigger tutorial
- [ ] Apply TutorialOverlay to main screens
- [ ] Add highlight IDs to UI components
- [ ] Implement navigation integration
### Phase 4: Settings Integration
- [ ] Create TutorialSettingsScreen
- [ ] Add restart functionality
- [ ] Test tutorial restart
### Phase 5: Testing & Polish
- [ ] End-to-end testing
- [ ] Bug fixes
- [ ] Documentation
## File Structure Created
```
app/src/main/java/com/fragne/circl_app/
├── tutorial/
│   ├── models/
│   │   ├── UserType.kt ✅
│   │   ├── OnboardingData.kt ✅
│   │   ├── TutorialStep.kt ✅
│   │   ├── TutorialFlow.kt ✅
│   │   ├── TutorialProgress.kt ✅
│   │   └── TutorialState.kt ✅
│   ├── content/
│   │   └── TutorialContentFactory.kt ✅
│   ├── ui/
│   │   ├── TutorialOverlay.kt ✅
│   │   ├── TutorialTooltip.kt ✅
│   │   ├── HighlightCutout.kt ✅
│   │   └── CommunityWelcomeOverlay.kt ✅
│   └── TutorialManager.kt ✅
```
## User Type Detection Mapping
Based on Page3 "Main Usage Interests":
- "Student" (alone) → Student
- "Student" + business → StudentEntrepreneur
- "Start Your Business" → Entrepreneur
- "Scale Your Business" → Entrepreneur
- "Network with Entrepreneurs" → Entrepreneur
- "Find Co-Founder/s" → Entrepreneur
- "Find Mentors" → Entrepreneur
- "Find Investors" → Entrepreneur
- "Make Investments" → Investor
- "Share Knowledge" → Mentor
- "Sell a Skill" → Entrepreneur
- "Be Part of the Community" → CommunityBuilder
## How It Works
1. User completes onboarding (Page3)
2. System detects user type from interests
3. User logs in
4. Tutorial automatically triggers
5. Personalized flow guides through app features
6. User can skip or complete tutorial
7. Settings allows tutorial restart
## Integration Examples
### In MainActivity or App entry point:
```kotlin
val tutorialManager = TutorialManager.getInstance(context)
```
### In Page3Screen (onboarding completion):
```kotlin
val onboardingData = OnboardingData(
    usageInterests = selectedUsageInterest ?: "",
    industryInterests = selectedIndustryInterest ?: "",
    location = "",
    userGoals = null
)
tutorialManager.detectAndSetUserType(onboardingData)
```
### In Main App screens:
```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    // Your screen content
    MainScreenContent()
    // Tutorial overlay
    TutorialOverlay(tutorialManager)
}
```
## Estimated Timeline
- Phase 1: Core (COMPLETED) ✅
- Phase 2: Onboarding (1-2 days)
- Phase 3: Main App (2-3 days)
- Phase 4: Settings (1 day)
- Phase 5: Testing (2-3 days)
**Total**: ~1 week remaining for full integration
## Notes
- All core functionality is implemented
- Ready for integration testing
- UI components are fully functional
- State management uses Kotlin StateFlow
- Persistence uses SharedPreferences
