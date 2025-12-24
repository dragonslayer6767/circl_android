# Tutorial Navigation - Quick Test Guide

## How to Test Tutorial Navigation

### Step 1: Launch the App
Open the Circl app and make sure you're logged in and at the main screen.

### Step 2: Navigate to Settings
Tap the Settings tab in the bottom navigation bar (rightmost icon).

### Step 3: Start Tutorial
1. Scroll down to the "Tutorial & Help" section
2. Tap "Start Tutorial"
3. A dialog will appear with a dropdown to select the tutorial type

### Step 4: Select Tutorial Type
Choose any tutorial type from the dropdown (try "Entrepreneur" first as it has the most comprehensive tutorial):
- Entrepreneur
- Student
- Student Entrepreneur
- Mentor
- Investor
- Community Builder
- Other

### Step 5: Start and Navigate
1. Tap "Start Tutorial" button
2. You should immediately be navigated to the Forum/Home screen
3. A tutorial overlay should appear with instructions
4. Tap "Next" or tap anywhere on the semi-transparent overlay

### Step 6: Observe Auto-Navigation
As you progress through the tutorial steps, the app should automatically navigate you to different screens:

**Entrepreneur Tutorial Flow**:
- Step 1: Forum/Home (bulletin board)
- Step 2: Network tab (co-founders & mentors)
- Step 3: Circles tab (business operations)
- Step 4-5: Circles tab (industry communities)
- Step 6: Growth Hub/More tab (business profile)
- And so on...

### Expected Behavior

✅ **Navigation should happen automatically** when you advance to a new step
✅ **The tutorial overlay should follow you** to each new screen
✅ **The bottom navigation should highlight** the correct tab as you navigate
✅ **Progress indicators should update** showing which step you're on
✅ **You can go back and forward** through tutorial steps

### What to Look For

1. **Smooth Transitions**: Navigation should feel seamless
2. **Correct Screen**: Each step should show you the relevant screen for that feature
3. **Overlay Visibility**: The tutorial tooltip should be clearly visible on each screen
4. **No Crashes**: The app should remain stable throughout the tutorial
5. **Completion**: At the end, the tutorial should close and return you to normal app usage

### Debugging

If navigation doesn't work, check the Logcat for these messages:
- `🎓 Started tutorial for [UserType]` - Tutorial started
- `⏭️ Advanced to step X/Y` - Step changed
- `🧭 Navigating to: [destination]` - Navigation triggered
- `Navigation callback set` - Callback properly registered

### Common Issues & Solutions

**Issue**: Tutorial starts but doesn't navigate
- **Solution**: Make sure you're tapping "Next" or the overlay to advance steps

**Issue**: Tutorial overlay doesn't show
- **Solution**: Check that TutorialOverlay is properly included in MainScreen

**Issue**: Navigation goes to wrong screen
- **Solution**: Check the destination mapping in RootNavigation.kt

**Issue**: Tutorial gets stuck
- **Solution**: You can always skip the tutorial or navigate away manually

### Testing All User Types

For comprehensive testing, try each user type:
1. **Entrepreneur**: Most complete tutorial (9+ steps)
2. **Student**: Focus on learning and mentorship
3. **Student Entrepreneur**: Balanced approach
4. **Mentor**: Mentorship-focused
5. **Investor**: Investment opportunities
6. **Community Builder**: General overview
7. **Other**: Basic features

Each should navigate through relevant screens for that persona.

## Manual Navigation Test

You can also manually test navigation by:
1. Starting any tutorial
2. Using the device back button - should work normally
3. Manually tapping bottom nav tabs - tutorial should pause
4. Resuming tutorial - should pick up where you left off

---
**Last Updated**: December 24, 2024

