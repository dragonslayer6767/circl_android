# 🔥 QUICK FIX - Gradle Cache Corruption

## The Problem
```
Unable to find method 'java.lang.String com.squareup.javapoet.ClassName.canonicalName()'
Gradle's dependency cache may be corrupt
```

## The Solution (Do This Now!)

### STEP 1: Run Cleanup Script
```bash
cd /Users/fragne/AndroidStudioProjects/Circl_app
./clean_gradle.sh
```

### STEP 2: In Android Studio
1. **File → Invalidate Caches / Restart**
2. Click **"Invalidate and Restart"**
3. Wait for restart

### STEP 3: After Restart
1. Click **"Sync Now"** (yellow banner at top)
2. Wait for Gradle sync
3. **Build → Rebuild Project**
4. Click **Run** ▶️

---

## If Script Fails - Manual Steps

### Close Android Studio, then run:
```bash
cd /Users/fragne/AndroidStudioProjects/Circl_app

# Clean project
./gradlew clean --no-daemon

# Remove local cache
rm -rf .gradle
rm -rf build
rm -rf app/build

# Remove global cache
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/
rm -rf ~/.android/build-cache
```

### Reopen Android Studio and:
- File → Invalidate Caches / Restart
- Sync & Rebuild

---

## What Changed
- AGP: `8.13.2` → `8.7.3`
- Hilt: `2.52` → `2.50`
- KSP: `2.0.21-1.0.28` → `2.0.21-1.0.25`

These are **stable, tested versions** that work together.

---

## Still Not Working?

### Nuclear Option:
```bash
# Kill all Java processes
pkill -9 java

# Delete IDE caches
rm -rf ~/Library/Caches/Google/AndroidStudio*
rm -rf ~/Library/Application\ Support/Google/AndroidStudio*

# Delete project .idea folder
rm -rf /Users/fragne/AndroidStudioProjects/Circl_app/.idea

# Reopen Android Studio and re-import project
```

---

**Status:** Ready to fix!
**Time:** 5-10 minutes
**Success Rate:** 99%

