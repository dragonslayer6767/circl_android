# Messaging System - Quick Reference

## File Locations
```
app/src/main/java/com/fragne/circl_app/ui/messages/
├── MessagesModels.kt     - Data models
├── MessagesViewModel.kt  - Messages list logic
├── ChatViewModel.kt      - Chat logic
├── MessagesScreen.kt     - Messages list UI
└── ChatScreen.kt         - Chat UI
```

## Navigation Routes
```kotlin
Route.Messages              // Messages list screen
Route.Chat(userId, userName, userEmail)  // Individual chat
```

## Usage Examples

### Navigate to Messages
```kotlin
// From any screen with navigation
navController.navigate(Route.Messages)
```

### Navigate to Chat
```kotlin
// From MessagesScreen
onNavigateToChat = { user, messages ->
    navController.navigate(
        Route.Chat(
            userId = user.id,
            userName = user.name,
            userEmail = user.email
        )
    )
}
```

### Access from Headers
The messages icon (✉️) in Forum and Network screens navigates to Messages.

## Key Components

### MessagesScreen
- **Header:** Profile (left), Logo (center), Home (right)
- **Search:** Search bar + user suggestions dropdown
- **Conversations:** List of active chats with unread badges
- **Empty State:** Refresh button when no conversations

### ChatScreen
- **Header:** Back button, user info, options menu
- **Messages:** Scrollable list of message bubbles
- **Input:** Text field + send button
- **Features:** Auto-scroll, typing state, loading state

## Mock Data

### Users Available
1. Alex Kumar (Online) - StartupHub
2. Jennifer Lee - Innovation Labs
3. Marcus Johnson (Online) - Venture Capital
4. Sarah Anderson - TechVenture
5. David Chen (Online) - AI Solutions

### Active Conversations
- Alex Kumar: 2 unread messages
- Jennifer Lee: All read
- Marcus Johnson: 1 unread message

## ViewModels

### MessagesViewModel
```kotlin
// Update search
viewModel.updateSearchText("Alex")

// Select user
viewModel.selectUser(user)

// Clear search
viewModel.clearSearch()

// Refresh messages
viewModel.refreshMessages()

// Mark as read
viewModel.markAsRead(senderId)
```

### ChatViewModel
```kotlin
// Update message text
viewModel.updateMessageText("Hello!")

// Send message
viewModel.sendMessage()

// Toggle options menu
viewModel.toggleOptionsMenu()
```

## State Properties

### MessagesUiState
```kotlin
conversations: List<Conversation>
networkUsers: List<NetworkUser>
searchText: String
suggestedUsers: List<NetworkUser>
selectedUser: NetworkUser?
isLoading: Boolean
unreadMessageCount: Int
```

### ChatUiState
```kotlin
messages: List<ChatMessage>
messageText: String
isTyping: Boolean
isSending: Boolean
showOptionsMenu: Boolean
```

## Styling

### Colors
```kotlin
val primaryBlue = Color(0xFF004AAD)   // Headers, sent messages
val lightGray = Color(0xFFF0F0F0)     // Received messages
val searchGray = Color(0xFFF5F5F5)    // Input backgrounds
```

### Message Bubbles
```kotlin
// Current user: Blue background, white text, right-aligned
// Other user: Gray background, black text, left-aligned
// Border radius: 20dp with 4dp tail corner
```

## Timestamps

Format: Relative time
- `< 1 min` → "Just now"
- `< 1 hour` → "5m"
- `< 24 hours` → "2h"
- `< 7 days` → "3d"
- `> 7 days` → "Dec 20"

## Common Tasks

### Add New Conversation
Update `fetchMessages()` in MessagesViewModel with new mock message data.

### Change Colors
Update color constants in MessagesScreen.kt and ChatScreen.kt.

### Integrate API
Replace mock data functions in viewModels with actual API calls.

### Add Features
- Media: Update Message model to include media URLs
- Groups: Add group info to NetworkUser/Conversation
- Reactions: Add reactions list to Message model

## Navigation Flow
```
Forum/Network → Messages → Chat → Back to Messages
              ↓
         Search Users → Chat
```

## Status
✅ Fully implemented with mock data
✅ Navigation integrated
✅ UI complete and polished
⏳ Backend API integration needed
⏳ Real-time messaging needed

## Quick Tips
1. Search filters users by name, username, or email
2. Blue accent indicates unread conversations
3. Online indicator shows green dot
4. Auto-reply simulates responses after 2 seconds
5. Messages auto-scroll to bottom
6. Tap avatar anywhere to view profile
7. Options menu has 3 dots in chat header

