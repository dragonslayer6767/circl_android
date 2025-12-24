# Circl Android App

This is the Android version of the Circl iOS app, built with Jetpack Compose and Kotlin.

## Project Structure

```
app/src/main/java/com/fragne/circl_app/
├── CirclApplication.kt          # Hilt Application class
├── MainActivity.kt              # Main activity entry point
├── core/
│   ├── data/
│   │   ├── APIConfig.kt        # API configuration (iOS: APIConfig.swift)
│   │   └── AppStateManager.kt  # App state management (iOS: AppState.swift)
│   ├── domain/
│   │   └── SharedDataModels.kt # Data models (iOS: SharedDataModels.swift)
│   └── util/
├── data/
│   ├── remote/                  # Retrofit API services
│   ├── local/                   # Room database
│   └── repository/              # Repository pattern implementations
├── di/
│   └── AppModule.kt            # Hilt dependency injection
└── ui/
    ├── navigation/              # Navigation setup (iOS: RootSwitcher.swift)
    ├── theme/                   # Material3 theming
    ├── components/              # Reusable UI components
    ├── onboarding/              # Login/signup screens
    ├── network/                 # Networking/feed features
    ├── circles/                 # Circle management
    ├── businesses/              # Business profiles
    ├── forum/                   # Forum discussions
    ├── settings/                # Settings screens
    ├── profile/                 # User profiles
    ├── notifications/           # Notifications
    ├── subscription/            # Paywall/subscriptions
    └── tutorial/                # Tutorial screens
```

## iOS to Android Mapping

### Core Architecture
- **iOS AppState** → **Android AppStateManager** (using DataStore)
- **iOS @Published** → **Android StateFlow**
- **iOS ObservableObject** → **Android ViewModel**
- **iOS UserDefaults** → **Android DataStore Preferences**
- **iOS URLSession** → **Android Retrofit + OkHttp**

### UI Framework
- **SwiftUI Views** → **Jetpack Compose Composables**
- **@State** → **remember/mutableStateOf**
- **@StateObject** → **viewModel() / hiltViewModel()**
- **NavigationStack** → **NavHost + NavController**
- **Sheet** → **ModalBottomSheet / Dialog**

### Key Files Mapping
| iOS File | Android Equivalent |
|----------|-------------------|
| `APIConfig.swift` | `core/data/APIConfig.kt` |
| `AppState.swift` | `core/data/AppStateManager.kt` |
| `SharedDataModels.swift` | `core/domain/SharedDataModels.kt` |
| `RootSwitcher.swift` | `ui/navigation/RootNavigation.kt` |
| `ContentView.swift` | `MainActivity.kt` + Navigation |
| ViewModels | `ui/*/ViewModel.kt` classes |

## Tech Stack

### Core
- **Kotlin** - Primary language
- **Jetpack Compose** - UI framework
- **Material3** - Design system
- **Hilt** - Dependency injection
- **Kotlin Coroutines & Flow** - Asynchronous programming

### Networking
- **Retrofit** - HTTP client
- **OkHttp** - HTTP/HTTPS transport
- **Kotlinx Serialization** - JSON parsing

### Storage
- **DataStore** - Key-value storage (replaces UserDefaults)
- **Room** - Local database (SQLite)

### Image Loading
- **Coil** - Image loading library

### Navigation
- **Navigation Compose** - Type-safe navigation

## Backend Integration

The app connects to a Django backend at:
- **Development (Emulator)**: `http://10.0.2.2:8000/api/`
- **Development (Device)**: `http://127.0.0.1:8000/api/`
- **Production**: `https://circlapp.online/api/`

Configure the environment in `core/data/APIConfig.kt`.

## Getting Started

1. **Sync Gradle** - Open project in Android Studio and sync
2. **Run on Emulator** - Default API points to `10.0.2.2:8000` for local Django server
3. **Run on Device** - Change `APIConfig.BASE_URL` to `DEVICE_BASE_URL` or point to production

## Current Implementation Status

### ✅ Completed
- Project structure and package organization
- Hilt dependency injection setup
- Core data models (SharedDataModels)
- API configuration
- App state management (login/logout state)
- Navigation framework
- Basic authentication UI (Login/Onboarding screens)
- MainActivity with Compose setup

### 🚧 In Progress
- API service implementations
- Authentication repository
- Main app screens (Network, Circles, Forum, More)

### 📋 TODO
- Network/Feed screen
- Circle management screens
- Business profile screens
- Forum discussion screens
- User profile screens
- Settings screens
- Notifications system
- Subscription/Paywall
- Tutorial screens
- Adaptive layouts for tablets
- Image picker
- Push notifications (Firebase Cloud Messaging)

## Development Workflow

To translate iOS screens to Android:

1. **Share iOS SwiftUI file** (copy/paste or attach)
2. **Identify components**:
   - Views → Composables
   - ViewModels → Android ViewModels
   - API calls → Retrofit services
3. **Create Android equivalents**:
   - Screen composable in `ui/[feature]/`
   - ViewModel in same package
   - API service in `data/remote/`
   - Repository in `data/repository/`
4. **Wire up navigation** in `RootNavigation.kt`

## Notes

- The Android app is **purely frontend** - all backend logic is in Django
- Use **Hilt** for all dependency injection
- Follow **MVVM pattern** (Model-View-ViewModel)
- Use **Repository pattern** for data layer
- All API responses should match Django backend structure
- Use **StateFlow** for reactive state management

## Contact

For questions about iOS implementation, refer to the iOS project at:
`/Users/fragne/Documents/GitHub/Circl_app`

