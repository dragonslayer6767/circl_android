# Tutorial Navigation Flow - Visual Guide

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         Settings Screen                          │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Tutorial & Help Section                               │    │
│  │  ┌──────────────────────────────────────────────────┐  │    │
│  │  │  Start Tutorial                                   │  │    │
│  │  │  [Entrepreneur Tutorial]                          │  │    │
│  │  └──────────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User taps "Start Tutorial"
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      TutorialManager                             │
│                                                                   │
│  restartTutorial(userType)                                      │
│    ↓                                                             │
│  getTutorialFlow(userType) → Load tutorial steps                │
│    ↓                                                             │
│  _currentFlow = flow                                            │
│  _currentStepIndex = 0                                          │
│  _isShowingTutorial = true                                      │
│    ↓                                                             │
│  handleStepNavigation()                                         │
│    ↓                                                             │
│  navigationCallback?.invoke(destination)                        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    RootNavigation - MainScreen                   │
│                                                                   │
│  LaunchedEffect(mainNavController) {                            │
│    tutorialManager.setNavigationCallback { destination ->       │
│      when (destination) {                                       │
│        "PageForum" → mainNavController.navigate(Route.Forum)    │
│        "PageUnifiedNetworking" → navigate(Route.Network)        │
│        "PageCircles" → navigate(Route.Circles)                  │
│        // ... etc                                                │
│      }                                                           │
│    }                                                             │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      Navigate to Screen                          │
│                                                                   │
│  mainNavController.navigate(Route.Forum) {                      │
│    popUpTo(graph.findStartDestination().id) {                   │
│      saveState = true                                           │
│    }                                                             │
│    launchSingleTop = true                                       │
│    restoreState = true                                          │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      Screen + Tutorial Overlay                   │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │                                                             │ │
│  │              Forum Screen Content                           │ │
│  │                                                             │ │
│  │  ┌─────────────────────────────────────────────────────┐  │ │
│  │  │   Tutorial Overlay (Semi-transparent)               │  │ │
│  │  │                                                       │  │ │
│  │  │   ┌───────────────────────────────────────────┐    │  │ │
│  │  │   │  Step 1/9: Your Entrepreneurial Bulletin  │    │  │ │
│  │  │   │  Board                                      │    │  │ │
│  │  │   │                                            │    │  │ │
│  │  │   │  Share updates and see what others are    │    │  │ │
│  │  │   │  posting in the community...              │    │  │ │
│  │  │   │                                            │    │  │ │
│  │  │   │  [Previous]  [Skip]  [Next]               │    │  │ │
│  │  │   └───────────────────────────────────────────┘    │  │ │
│  │  └─────────────────────────────────────────────────────┘  │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User taps "Next"
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      TutorialManager                             │
│                                                                   │
│  nextStep()                                                      │
│    ↓                                                             │
│  _currentStepIndex++                                            │
│    ↓                                                             │
│  handleStepNavigation()                                         │
│    ↓                                                             │
│  navigationCallback?.invoke("PageUnifiedNetworking")            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
              [Navigate to Network Screen]
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                   Network Screen + Tutorial Overlay              │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │                                                             │ │
│  │              Network Screen Content                         │ │
│  │                                                             │ │
│  │  ┌─────────────────────────────────────────────────────┐  │ │
│  │  │   Tutorial Overlay                                  │  │ │
│  │  │                                                       │  │ │
│  │  │   ┌───────────────────────────────────────────┐    │  │ │
│  │  │   │  Step 2/9: Find Co-Founders & Mentors     │    │  │ │
│  │  │   │                                            │    │  │ │
│  │  │   │  Connect with potential co-founders...    │    │  │ │
│  │  │   │                                            │    │  │ │
│  │  │   │  [Previous]  [Skip]  [Next]               │    │  │ │
│  │  │   └───────────────────────────────────────────┘    │  │ │
│  │  └─────────────────────────────────────────────────────┘  │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘

... and so on for each tutorial step ...
```

## State Management

```
TutorialManager State:
┌─────────────────────────────────────────┐
│ _currentFlow: TutorialFlow?            │  → Currently active tutorial
│ _currentStepIndex: Int                 │  → Current step number (0-based)
│ _isShowingTutorial: Boolean            │  → Show/hide overlay
│ _tutorialState: TutorialState          │  → NotStarted/InProgress/Completed
│ _userType: UserType                    │  → User's detected type
│ navigationCallback: ((String) -> Unit)?│  → Navigation function
└─────────────────────────────────────────┘
```

## Tutorial Step Structure

```kotlin
TutorialStep(
    title = "Welcome to Circl!",
    description = "Let's show you around",
    targetView = "main_navigation",
    message = "Detailed explanation...",
    navigationDestination = "PageForum",  // ← This triggers navigation!
    highlightRect = null,
    tooltipAlignment = TooltipAlignment.CENTER,
    duration = null,
    isInteractive = false
)
```

## Navigation Mapping

```
Tutorial Destinations → App Routes
═══════════════════════════════════
PageForum              → Route.Forum (Home tab)
PageUnifiedNetworking  → Route.Network
PageCircles            → Route.Circles
PageBusinessProfile    → Route.More (Growth Hub)
PageEntrepreneurRes... → Route.More (Growth Hub)
PageMessages           → Route.Messages
PageSettings           → Route.Settings
```

## Complete Flow Example (Entrepreneur Tutorial)

```
Step 0: Settings Screen
        ↓ (Start Tutorial)
Step 1: Forum Screen        [PageForum]
        ↓ (Next)
Step 2: Network Screen      [PageUnifiedNetworking]
        ↓ (Next)
Step 3: Circles Screen      [PageCircles]
        ↓ (Next)
Step 4: Circles Screen      [null - stays on same screen]
        ↓ (Next)
Step 5: Circles Screen      [null - stays on same screen]
        ↓ (Next)
Step 6: Growth Hub Screen   [PageBusinessProfile]
        ↓ (Next)
Step 7: Growth Hub Screen   [PageEntrepreneurResources]
        ↓ (Next)
Step 8: Messages Screen     [PageMessages]
        ↓ (Next)
Step 9: Forum Screen        [PageForum]
        ↓ (Complete)
Tutorial ends, user continues using app normally
```

## Key Components Interaction

```
                    ┌─────────────────┐
                    │  SettingsScreen │
                    └────────┬────────┘
                             │ triggers
                             ↓
                    ┌─────────────────┐
                    │ TutorialManager │←──────────┐
                    └────────┬────────┘           │
                             │ invokes            │
                             ↓                    │ sets callback
                    ┌─────────────────┐           │
                    │ navigationCal.. │           │
                    └────────┬────────┘           │
                             │                    │
                             ↓                    │
                    ┌─────────────────┐           │
                    │  RootNavigation │───────────┘
                    │   (MainScreen)  │
                    └────────┬────────┘
                             │ navigates
                             ↓
        ┌───────────────┬────────────┬────────────┬──────────┐
        ↓               ↓            ↓            ↓          ↓
    ForumScreen   NetworkScreen CirclesScreen MoreScreen Settings
        │               │            │            │          │
        └───────────────┴────────────┴────────────┴──────────┘
                             │
                             ↓
                    ┌─────────────────┐
                    │ TutorialOverlay │ (shown on all screens)
                    └─────────────────┘
```

---
**Visual Flow Created**: December 24, 2024

