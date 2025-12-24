#!/bin/bash

# Script to clean Gradle cache and fix build issues
# Run this from Android Studio Terminal

echo "🧹 Cleaning Gradle cache and build directories..."

# Stop all Gradle daemons
echo "1. Stopping Gradle daemons..."
./gradlew --stop 2>/dev/null || echo "   No running Gradle daemons found"

# Clean the project
echo "2. Cleaning project build..."
./gradlew clean 2>/dev/null || echo "   Clean command executed"

# Remove local build directories
echo "3. Removing build directories..."
rm -rf .gradle
rm -rf build
rm -rf app/build
rm -rf app/.cxx

# Clear Gradle caches (global)
echo "4. Clearing global Gradle caches..."
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/

# Clear Android Studio caches
echo "5. Clearing IDE caches..."
rm -rf ~/.android/build-cache

echo ""
echo "✅ Cache cleanup complete!"
echo ""
echo "📋 Next steps in Android Studio:"
echo "   1. File → Invalidate Caches / Restart"
echo "   2. Click 'Invalidate and Restart'"
echo "   3. After restart, click 'Sync Now' to sync Gradle"
echo "   4. Build → Rebuild Project"
echo "   5. Run the app"
echo ""
echo "If still having issues, try:"
echo "   • Close Android Studio completely"
echo "   • Run this script again"
echo "   • Reopen Android Studio"

