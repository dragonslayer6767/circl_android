# How to Build and Run the App

## Current Status
✅ Fixed Gradle build configuration (compileSdk syntax)
✅ Updated Java version to 17 for compatibility with JDK 21
✅ Temporarily disabled Hilt to avoid JavaPoet dependency conflict
✅ All onboarding screens implemented (Page1-Page19)
✅ Navigation flow configured properly

## Build Configuration Fixed
1. **JDK Configuration**: Set to jbr-21 in Android Studio settings
2. **Gradle Files**: Fixed syntax errors in build.gradle.kts
3. **Hilt Issue**: Temporarily commented out to avoid dependency conflicts

## To Build and Run in Android Studio:

### Step 1: Sync Project
1. Open Android Studio
2. Go to **File → Sync Project with Gradle Files**
3. Wait for sync to complete

### Step 2: Build the App
1. Go to **Build → Make Project** (Cmd+F9)
2. Wait for build to complete
3. Check for any errors in the Build Output

### Step 3: Run on Emulator
1. Make sure your emulator is running
2. Click the **Run** button (green play button) or press **Ctrl+R**
3. Select your emulator device
4. Wait for app to install and launch

## If Emulator Doesn't Show the App:

### Check if app is installed:
```bash
adb devices  # Should show your emulator
adb shell pm list packages | grep circl_app
```

### If app is installed but not launching:
```bash
adb shell am start -n com.fragne.circl_app/.MainActivity
```

### If app crashes on launch:
```bash
adb logcat | grep -E "(AndroidRuntime|circl_app)"
```

### Force reinstall:
```bash
adb uninstall com.fragne.circl_app
```
Then click Run again in Android Studio.

## Onboarding Flow Implemented:
- **Page1**: Login/Entry screen (start screen)
- **Page17**: Ethics screen
- **Page14**: Age verification
- **Page3**: Gender selection
- **Page4**: Name input
- **Page5**: Birthday selection
- **Page13**: Circle creation
- **Page19**: Welcome screen (final)

## Known Issues & Workarounds:
1. **Hilt Dependency Conflict**: 
   - Temporarily disabled Hilt to avoid JavaPoet version conflict
   - Will re-enable once we upgrade to compatible versions
   - For now, using regular ViewModels without injection

2. **AppStateManager & AppModule**:
   - Backed up to .bak files (not in build)
   - Will restore when Hilt is re-enabled

## Next Steps:
1. ✅ Build the app in Android Studio
2. ✅ Run on emulator
3. Test onboarding flow
4. Fix Hilt dependency issue properly
5. Implement Django backend integration

## Terminal Build (Alternative):
If you prefer terminal, run:
```bash
cd /Users/fragne/AndroidStudioProjects/Circl_app
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew clean assembleDebug
```

Then install:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting:

### If "Invalid Gradle JDK" error appears:
1. Go to **Settings → Build, Execution, Deployment → Build Tools → Gradle**
2. Set **Gradle JDK** to **jbr-21** or **Embedded JDK**
3. Click Apply

### If build fails with "Unresolved reference":
Make sure all Hilt-related code is commented out as shown in the attached files.

### If emulator freezes:
```bash
adb kill-server
adb start-server
```

Then restart the emulator.

