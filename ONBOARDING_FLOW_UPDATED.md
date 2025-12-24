# Onboarding Flow Updated

## Date: December 24, 2025

## Changes Made

### New Onboarding Flow
**Page17 (Ethics Screen) has been removed from the onboarding process.**

The new onboarding flow is:
```
Page1 → Page14 → Page3 → Page4 → Page5 → Page13 → Page19 → Main App
```

### Previous Flow (for reference)
```
Page1 → Page17 → Page14 → Page3 → Page4 → Page5 → Page13 → Page19 → Main App
```

## Detailed Flow

1. **Page1** - Login/Entry Screen
   - User can login or click "Join Circl"
   - Clicking "Join Circl" now navigates directly to Page14 (skipping Page17)

2. **Page14** - Terms & Conditions
   - User accepts terms
   - Navigates to Page3

3. **Page3** - User Information Form
   - Basic user info (name, email, etc.)
   - Navigates to Page4

4. **Page4** - Profile Picture
   - User uploads profile picture
   - Navigates to Page5

5. **Page5** - Personal Information
   - Birthday, location, gender, availability, personality type
   - Navigates to Page13

6. **Page13** - Notifications Permission
   - Request notification permissions
   - Navigates to Page19

7. **Page19** - Welcome/Completion
   - Final welcome screen
   - Completes onboarding and navigates to main app (Network screen)

## Files Modified

### `/app/src/main/java/com/fragne/circl_app/ui/navigation/RootNavigation.kt`
- Updated comment documenting the flow
- Changed Page1's `onNavigateToSignup` to navigate to `Route.Page14` instead of `Route.Page17`
- Removed the Page17 composable route entirely

## Page17 Status

- **Page17Screen.kt** file still exists in the codebase but is no longer used
- Route.Page17 still exists in Route.kt but is not used in navigation
- Can be removed in future cleanup if desired

## Testing

To test the new flow:
1. Clear app data or use a fresh install
2. Launch the app
3. Click "Join Circl" on Page1
4. Verify it goes directly to Page14 (Terms & Conditions)
5. Continue through the flow to ensure all pages work correctly

## Benefits

- Shorter onboarding process
- Faster user registration
- Ethics content can be moved to terms if needed
- One less screen for users to click through

