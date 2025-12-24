# iOS SwiftUI to Android Compose - Quick Reference

## Common Patterns Translation

### Basic UI Components

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `Text("Hello")` | `Text("Hello")` |
| `Button("Click") { }` | `Button(onClick = { }) { Text("Click") }` |
| `Image(systemName: "icon")` | `Icon(Icons.Default.IconName, contentDescription = null)` |
| `TextField("Hint", text: $text)` | `TextField(value = text, onValueChange = { text = it }, label = { Text("Hint") })` |
| `VStack { }` | `Column { }` |
| `HStack { }` | `Row { }` |
| `ZStack { }` | `Box { }` |
| `Spacer()` | `Spacer()` |
| `Divider()` | `Divider()` |
| `ScrollView { }` | `LazyColumn { items(...) { } }` |
| `List { }` | `LazyColumn { items(...) { } }` |

### Modifiers

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `.padding()` | `.padding()` |
| `.background(Color.red)` | `.background(Color.Red)` |
| `.foregroundColor(.blue)` | `.color(Color.Blue)` (for Text) |
| `.frame(width: 100, height: 50)` | `.size(width = 100.dp, height = 50.dp)` |
| `.cornerRadius(10)` | `.clip(RoundedCornerShape(10.dp))` |
| `.shadow(radius: 5)` | `.shadow(elevation = 5.dp)` |
| `.font(.headline)` | `style = MaterialTheme.typography.headlineSmall` |
| `.bold()` | `fontWeight = FontWeight.Bold` |

### State Management

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `@State var text = ""` | `var text by remember { mutableStateOf("") }` |
| `@Binding var isOn: Bool` | `isOn: Boolean, onToggle: (Boolean) -> Unit` |
| `@StateObject var vm = VM()` | `val vm: VM = hiltViewModel()` |
| `@ObservedObject var vm: VM` | `val state by vm.uiState.collectAsState()` |
| `@EnvironmentObject var state: AppState` | `val appState: AppStateManager = hiltViewModel()` |
| `@Published var items: [Item]` | `private val _items = MutableStateFlow<List<Item>>(emptyList())` |
| | `val items: StateFlow<List<Item>> = _items.asStateFlow()` |

### Navigation

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `NavigationStack { }` | `NavHost(navController, startDestination) { }` |
| `NavigationLink("Go", destination: View())` | `navController.navigate(Route.Destination)` |
| `.navigationTitle("Title")` | `Scaffold(topBar = { TopAppBar { Text("Title") } })` |
| `.sheet(isPresented: $show) { }` | `ModalBottomSheet(onDismissRequest = { }) { }` |
| `.alert(isPresented: $show) { }` | `AlertDialog(onDismissRequest = { }) { }` |

### Lifecycle & Effects

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `.onAppear { }` | `LaunchedEffect(Unit) { }` |
| `.onDisappear { }` | `DisposableEffect(Unit) { onDispose { } }` |
| `.onChange(of: value) { }` | `LaunchedEffect(value) { }` |
| `.task { }` | `LaunchedEffect(Unit) { }` |

### Async/Await

| iOS Swift | Android Kotlin |
|----------|---------------|
| `Task { await fetchData() }` | `viewModelScope.launch { fetchData() }` |
| `async/await` | `suspend fun` with coroutines |
| `@MainActor` | `withContext(Dispatchers.Main)` |
| `URLSession` | `Retrofit` |

### Data Persistence

| iOS Swift | Android Kotlin |
|----------|---------------|
| `UserDefaults.standard` | `DataStore.preferences` |
| `@AppStorage("key")` | `dataStore.data.map { it[key] }` |
| `CoreData` | `Room Database` |
| `FileManager` | `Context.filesDir` |

### Networking

| iOS Swift | Android Kotlin |
|----------|---------------|
| `URLSession.shared.dataTask` | `Retrofit + OkHttp` |
| `Codable` | `@Serializable` (Kotlinx Serialization) |
| `JSONDecoder()` | `Json.decodeFromString()` |
| `JSONEncoder()` | `Json.encodeToString()` |

### Property Wrappers → Patterns

```swift
// iOS
class ViewModel: ObservableObject {
    @Published var users: [User] = []
    @Published var isLoading = false
    
    func fetchUsers() async {
        isLoading = true
        // fetch data
        users = fetchedUsers
        isLoading = false
    }
}
```

```kotlin
// Android
data class UiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false
)

class ViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val users = repository.fetchUsers()
            _uiState.update { it.copy(users = users, isLoading = false) }
        }
    }
}
```

### Image Loading

```swift
// iOS
AsyncImage(url: URL(string: imageUrl)) { image in
    image.resizable()
} placeholder: {
    ProgressView()
}
```

```kotlin
// Android
AsyncImage(
    model = imageUrl,
    contentDescription = null,
    placeholder = painterResource(R.drawable.placeholder),
    modifier = Modifier.size(100.dp)
)
```

### Lists

```swift
// iOS
List(users) { user in
    UserRow(user: user)
}
```

```kotlin
// Android
LazyColumn {
    items(users) { user ->
        UserRow(user = user)
    }
}
```

### Forms

```swift
// iOS
Form {
    Section("Profile") {
        TextField("Name", text: $name)
        TextField("Email", text: $email)
    }
}
```

```kotlin
// Android
Column(modifier = Modifier.padding(16.dp)) {
    Text("Profile", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") }
    )
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") }
    )
}
```

### Conditional Rendering

```swift
// iOS
if isLoading {
    ProgressView()
} else {
    ContentView()
}
```

```kotlin
// Android
if (isLoading) {
    CircularProgressIndicator()
} else {
    ContentView()
}
```

### Gestures

| iOS SwiftUI | Android Compose |
|------------|----------------|
| `.onTapGesture { }` | `.clickable { }` |
| `.onLongPressGesture { }` | `.pointerInput(Unit) { detectTapGestures(onLongPress = { }) }` |
| `.gesture(DragGesture())` | `.draggable(...)` |

### Colors

```swift
// iOS
Color.blue
Color(red: 0.5, green: 0.5, blue: 1.0)
Color("CustomColor") // Asset catalog
```

```kotlin
// Android
Color.Blue
Color(0xFF8080FF) // ARGB hex
MaterialTheme.colorScheme.primary
```

### Typography

```swift
// iOS
Text("Hello")
    .font(.headline)
    .fontWeight(.bold)
```

```kotlin
// Android
Text(
    text = "Hello",
    style = MaterialTheme.typography.headlineSmall,
    fontWeight = FontWeight.Bold
)
```

## Dependency Injection

```swift
// iOS - Manual DI or SwiftUI Environment
@EnvironmentObject var appState: AppState

// In App
ContentView()
    .environmentObject(AppState())
```

```kotlin
// Android - Hilt
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()

// In Composable
val viewModel: MyViewModel = hiltViewModel()
```

## Error Handling

```swift
// iOS
do {
    let data = try await fetchData()
    users = data
} catch {
    errorMessage = error.localizedDescription
}
```

```kotlin
// Android
viewModelScope.launch {
    try {
        val data = repository.fetchData()
        _uiState.update { it.copy(users = data) }
    } catch (e: Exception) {
        _uiState.update { it.copy(errorMessage = e.message) }
    }
}
```

## View Protocols → Interfaces

```swift
// iOS
protocol ProfileViewDelegate {
    func didUpdateProfile()
}
```

```kotlin
// Android
interface ProfileCallback {
    fun onProfileUpdated()
}

// Or use lambda
onProfileUpdated: () -> Unit
```

## Quick Tips

1. **Units**: iOS uses points, Android uses `dp` (density-independent pixels)
2. **Preview**: iOS `#Preview { }` → Android `@Preview @Composable fun PreviewName() { }`
3. **Strings**: iOS uses `NSLocalizedString` → Android uses `strings.xml` resources
4. **Assets**: iOS uses Asset Catalog → Android uses `res/drawable/`
5. **Threading**: iOS `@MainActor` → Android `Dispatchers.Main`
6. **Nullability**: Swift optionals (`String?`) are similar to Kotlin nullables (`String?`)

## Common Gotchas

1. **State Hoisting**: Compose prefers passing state down and events up
2. **Recomposition**: Composables can recompose frequently - use `remember` for expensive operations
3. **Side Effects**: Use `LaunchedEffect`, `DisposableEffect`, etc. for side effects
4. **Immutability**: Prefer immutable data classes and update with `.copy()`
5. **ViewModel Scope**: Use `viewModelScope` for coroutines, not `GlobalScope`

This guide should help you quickly translate iOS patterns to Android equivalents!

