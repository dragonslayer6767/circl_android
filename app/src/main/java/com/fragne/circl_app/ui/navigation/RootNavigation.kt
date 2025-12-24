package com.fragne.circl_app.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fragne.circl_app.ui.onboarding.*
import com.fragne.circl_app.ui.forum.ForumScreen
import com.fragne.circl_app.ui.network.NetworkScreen
import com.fragne.circl_app.ui.messages.MessagesScreen
import com.fragne.circl_app.ui.messages.ChatScreen
import com.fragne.circl_app.ui.messages.NetworkUser
import com.fragne.circl_app.ui.messages.Message
import com.fragne.circl_app.ui.marketplace.GrowthHubScreen
import com.fragne.circl_app.ui.settings.SettingsScreen
import com.fragne.circl_app.ui.settings.pages.*
import com.fragne.circl_app.ui.profile.ProfileScreen
import com.fragne.circl_app.ui.profile.BusinessProfileScreen
import com.fragne.circl_app.ui.circles.dashboard.CirclesScreen
import com.fragne.circl_app.ui.circles.dashboard.CircleData
import com.fragne.circl_app.ui.circles.dashboard.CircleChannelMessagesScreen
import com.fragne.circl_app.ui.circles.home.CircleGroupChatScreen
import com.fragne.circl_app.ui.circles.home.Channel
import com.fragne.circl_app.ui.circles.home.ChannelCategory
import com.fragne.circl_app.ui.circles.home.ManageChannelsScreen
import com.fragne.circl_app.ui.circles.home.MemberListScreen
import com.fragne.circl_app.ui.circles.home.DashboardMemberListScreen
import com.fragne.circl_app.ui.circles.home.CircleDuesScreen
import com.fragne.circl_app.ui.profile.DynamicProfilePreviewScreen
import com.fragne.circl_app.ui.profile.FullProfile
import com.fragne.circl_app.ui.loading.LoadingScreen

/**
 * Temporary holder for passing complex objects through navigation
 * TODO: Replace with proper state management or ViewModel approach
 */
object NavigationHelper {
    var currentChatUser: NetworkUser? = null
    var currentChatMessages: List<Message> = emptyList()
}

/**
 * Root navigation composable - Onboarding flow
 * Flow: Page1 → Page17 → Page14 → Page3 → Page4 → Page5 → Page13 → Page19
 */
@Composable
fun RootNavigation(
    viewModel: RootNavigationViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Loading // Always start with loading screen
    ) {
        // Loading Screen - First screen shown on app launch
        composable<Route.Loading> {
            LoadingScreen(
                onLoadingComplete = {
                    // Navigate to appropriate screen after loading
                    if (isLoggedIn) {
                        navController.navigate(Route.Network) {
                            popUpTo(Route.Loading) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Route.Page1) {
                            popUpTo(Route.Loading) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Page1 - Login/Entry Screen
        composable<Route.Page1> {
            Page1Screen(
                onNavigateToSignup = {
                    navController.navigate(Route.Page17) {
                        popUpTo(Route.Page1) { inclusive = false }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Route.Network) {
                        popUpTo(Route.Page1) { inclusive = true }
                    }
                }
            )
        }

        // Page17 - Ethics Screen
        composable<Route.Page17> {
            Page17Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page14)
                }
            )
        }

        // Page14 - Terms & Conditions
        composable<Route.Page14> {
            Page14Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page3)
                }
            )
        }

        // Page3 - User Information Form
        composable<Route.Page3> {
            Page3Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page4)
                }
            )
        }

        // Page4 - Profile Picture
        composable<Route.Page4> {
            Page4Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page5)
                }
            )
        }

        // Page5 - Personal Information
        composable<Route.Page5> {
            Page5Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page13)
                }
            )
        }

        // Page13 - Notifications Permission
        composable<Route.Page13> {
            Page13Screen(
                onNavigateToNext = {
                    navController.navigate(Route.Page19)
                }
            )
        }

        // Page19 - Welcome/Completion
        composable<Route.Page19> {
            Page19Screen(
                onComplete = {
                    navController.navigate(Route.Network) {
                        popUpTo(Route.Page1) { inclusive = true }
                    }
                }
            )
        }

        // Main app flow
        composable<Route.Network> {
            MainScreen(navController = navController)
        }

        // Messages screen
        composable<Route.Messages> {
            MessagesScreen(
                onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                onNavigateToHome = { navController.navigate(Route.Forum) },
                onNavigateToChat = { user, messages ->
                    // Store user data in helper before navigation
                    NavigationHelper.currentChatUser = user
                    NavigationHelper.currentChatMessages = messages

                    navController.navigate(
                        Route.Chat(
                            userId = user.id,
                            userName = user.name,
                            userEmail = user.email
                        )
                    )
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Route.ProfilePreview(userId.toIntOrNull() ?: 0))
                },
                onNavigateToNetwork = { navController.navigate(Route.Network) },
                onNavigateToCircles = { navController.navigate(Route.Circles) },
                onNavigateToGrowthHub = { navController.navigate(Route.More) },
                onNavigateToSettings = { navController.navigate(Route.Settings) }
            )
        }

        // Chat screen
        composable<Route.Chat> {
            // Retrieve user data from helper
            val user = NavigationHelper.currentChatUser ?: NetworkUser(
                id = "1",
                name = "Unknown User",
                username = "unknown",
                email = "unknown@example.com"
            )

            val messages = NavigationHelper.currentChatMessages

            ChatScreen(
                user = user,
                messages = messages,
                onNavigateBack = {
                    // Clear helper data
                    NavigationHelper.currentChatUser = null
                    NavigationHelper.currentChatMessages = emptyList()
                    navController.popBackStack()
                },
                onNavigateToProfile = { userId ->
                    navController.navigate(Route.ProfilePreview(userId.toIntOrNull() ?: 0))
                }
            )
        }

        // My Profile screen
        composable<Route.MyProfile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToBusinessProfile = { navController.navigate(Route.BusinessProfile(0)) },
                onNavigateToSettings = { navController.navigate(Route.Settings) },
                onNavigateToPremium = { /* TODO: Navigate to premium/subscription */ },
                onNavigateToHome = { navController.navigate(Route.Forum) },
                onNavigateToNetwork = { navController.navigate(Route.Network) },
                onNavigateToCircles = { navController.navigate(Route.Circles) },
                onNavigateToGrowthHub = { navController.navigate(Route.More) }
            )
        }

        // Profile Preview - Dynamic profile preview for network users
        composable<Route.ProfilePreview> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ProfilePreview>()

            // TODO: Fetch actual profile data from API
            // For now, using mock data
            val mockProfile = FullProfile(
                userId = route.userId,
                firstName = "John",
                lastName = "Doe",
                profileImage = null,
                bio = "Passionate entrepreneur and tech enthusiast. Building the future one project at a time.",
                birthday = "1990-01-15",
                educationLevel = "Bachelor's Degree",
                institutionAttended = "Stanford University",
                locations = listOf("San Francisco", "New York"),
                personalityType = "ENTJ",
                skillsets = listOf("Python", "Kotlin", "SwiftUI", "React"),
                certificates = listOf("AWS Certified", "Google Cloud Professional"),
                yearsOfExperience = 8,
                clubs = listOf("Tech Founders Club", "Y Combinator Alumni"),
                hobbies = listOf("Hiking", "Photography", "Reading"),
                entrepreneurialHistory = "Founded 3 startups, 2 successful exits. Currently building SaaS platform for small businesses.",
                connectionsCount = 450,
                circs = 1250
            )

            val currentUserId = 1 // TODO: Get from UserDefaults/ViewModel
            val isInNetwork = false // TODO: Check if user is in network

            DynamicProfilePreviewScreen(
                profileData = mockProfile,
                isInNetwork = isInNetwork,
                currentUserId = currentUserId,
                onNavigateBack = { navController.popBackStack() },
                onRemoveFriend = {
                    // TODO: Call API to remove friend
                },
                onBlockUser = {
                    // TODO: Call API to block user
                }
            )
        }

        // Business Profile screen
        composable<Route.BusinessProfile> {
            BusinessProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                onNavigateToSettings = { navController.navigate(Route.Settings) },
                onNavigateToHome = { navController.navigate(Route.Forum) },
                onNavigateToNetwork = { navController.navigate(Route.Network) },
                onNavigateToCircles = { navController.navigate(Route.Circles) },
                onNavigateToGrowthHub = { navController.navigate(Route.More) }
            )
        }

        // Forum screen (can also be accessed directly)
        composable<Route.Forum> {
            ForumScreen(
                onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                onNavigateToMessages = { navController.navigate(Route.Messages) },
                onNavigateToUserProfile = { userId -> navController.navigate(Route.ProfilePreview(userId)) },
                onNavigateToComments = { postId -> /* TODO: Navigate to comments */ }
            )
        }
    }
}

/**
 * Bottom navigation items - Home, Network, Circles, Growth Hub, Settings
 */
sealed class BottomNavItem(
    val route: Route,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem(
        route = Route.Forum, // Home shows ForumScreen
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Network : BottomNavItem(
        route = Route.Network,
        title = "Network",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )

    data object Circles : BottomNavItem(
        route = Route.Circles,
        title = "Circles",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.People
    )

    data object GrowthHub : BottomNavItem(
        route = Route.More, // Using More route temporarily for Growth Hub
        title = "Growth Hub",
        selectedIcon = Icons.Filled.AttachMoney,
        unselectedIcon = Icons.Outlined.AttachMoney
    )

    data object Settings : BottomNavItem(
        route = Route.Settings,
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

/**
 * Main screen with bottom navigation bar
 */
@Composable
fun MainScreen(
    navController: NavHostController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val tutorialManager = remember { com.fragne.circl_app.tutorial.TutorialManager.getInstance(context) }

    val mainNavController = rememberNavController()

    // Set up navigation callback for tutorial
    LaunchedEffect(mainNavController) {
        tutorialManager.setNavigationCallback { destination ->
            // Map tutorial destination strings to Route objects
            when (destination) {
                "PageForum" -> mainNavController.navigate(Route.Forum) {
                    popUpTo(mainNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                "PageUnifiedNetworking" -> mainNavController.navigate(Route.Network) {
                    popUpTo(mainNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                "PageCircles" -> mainNavController.navigate(Route.Circles) {
                    popUpTo(mainNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                "PageBusinessProfile", "PageEntrepreneurResources" -> {
                    // Navigate to More/Growth Hub for now
                    mainNavController.navigate(Route.More) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                "PageMessages" -> {
                    // Navigate to Messages screen via the main nav controller
                    navController.navigate(Route.Messages)
                }
                "PageSettings" -> mainNavController.navigate(Route.Settings) {
                    popUpTo(mainNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                else -> {
                    android.util.Log.w("TutorialNavigation", "Unknown destination: $destination")
                }
            }
        }
    }

    // Trigger tutorial check after app is ready
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500) // Wait for UI to settle
        tutorialManager.checkAndTriggerTutorial()
    }
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Network,
        BottomNavItem.Circles,
        BottomNavItem.GrowthHub,
        BottomNavItem.Settings
    )

    Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = navBackStackEntry?.destination?.route?.contains(item.route::class.simpleName ?: "") == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selected,
                            onClick = {
                                mainNavController.navigate(item.route) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = Route.Forum, // Start with Home (Forum)
            modifier = Modifier.padding(paddingValues)
        ) {
            // Home tab - Shows Forum screen
            composable<Route.Forum> {
                ForumScreen(
                    onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                    onNavigateToMessages = { navController.navigate(Route.Messages) },
                    onNavigateToUserProfile = { userId -> navController.navigate(Route.ProfilePreview(userId)) },
                    onNavigateToComments = { postId -> /* TODO */ }
                )
            }

            // Network tab - Full networking screen with entrepreneurs, mentors, and connections
            composable<Route.Network> {
                NetworkScreen(
                    onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                    onNavigateToMessages = { navController.navigate(Route.Messages) },
                    onNavigateToUserProfile = { userId ->
                        navController.navigate(Route.ProfilePreview(userId))
                    }
                )
            }

            // Circles tab - Circles screen
            composable<Route.Circles> {
                CirclesScreen(
                    onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                    onNavigateToMessages = { navController.navigate(Route.Messages) },
                    onNavigateToCircleChat = { circle ->
                        // Use mainNavController for navigation within MainScreen
                        mainNavController.navigate(
                            Route.CircleGroupChat(
                                circleId = circle.id,
                                circleName = circle.name
                            )
                        )
                    }
                )
            }

            // Circle Group Chat - Individual circle home/chat screen
            composable<Route.CircleGroupChat> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.CircleGroupChat>()

                // Create CircleData from route parameters
                val circle = CircleData(
                    id = route.circleId,
                    name = route.circleName,
                    industry = "",
                    memberCount = 0,
                    pricing = "",
                    description = "",
                    isPrivate = false,
                    profileImageUrl = ""
                )

                CircleGroupChatScreen(
                    circle = circle,
                    onNavigateBack = { mainNavController.popBackStack() },
                    onNavigateToChannel = { channelId ->
                        // Mock channel names - in production, get from ViewModel
                        val channelNames = mapOf(
                            1 to "welcome",
                            2 to "chats",
                            3 to "announcements",
                            4 to "design",
                            5 to "development"
                        )
                        val channelName = channelNames[channelId] ?: "channel"

                        mainNavController.navigate(
                            Route.CircleChannelMessages(
                                channelId = channelId,
                                channelName = channelName,
                                circleName = route.circleName
                            )
                        )
                    },
                    onNavigateToMembers = {
                        mainNavController.navigate(
                            Route.MemberList(
                                circleId = route.circleId,
                                circleName = route.circleName
                            )
                        )
                    },
                    onNavigateToDues = {
                        mainNavController.navigate(
                            Route.CircleDues(
                                circleId = route.circleId,
                                circleName = route.circleName
                            )
                        )
                    },
                    onNavigateToManageChannels = {
                        mainNavController.navigate(
                            Route.ManageChannels(
                                circleId = route.circleId,
                                circleName = route.circleName
                            )
                        )
                    }
                )
            }

            // Circle Channel Messages - Individual channel chat screen
            composable<Route.CircleChannelMessages> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.CircleChannelMessages>()

                // Create Channel object from route parameters
                val channel = Channel(
                    id = route.channelId,
                    name = route.channelName,
                    category = "General" // TODO: Pass category or get from ViewModel
                )

                CircleChannelMessagesScreen(
                    channel = channel,
                    circleName = route.circleName,
                    onNavigateBack = { mainNavController.popBackStack() },
                    onNavigateToMembers = {
                        // TODO: Navigate to members list screen
                    },
                    onNavigateToProfile = { userId ->
                        navController.navigate(Route.ProfilePreview(userId))
                    }
                )
            }

            // Manage Channels - Channel management screen
            composable<Route.ManageChannels> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.ManageChannels>()

                // TODO: Get actual channels and categories from ViewModel
                // For now, using mock data
                val mockChannels = listOf(
                    Channel(1, "#welcome", "General", route.circleId, 1, false),
                    Channel(2, "#announcements", "General", route.circleId, 2, true),
                    Channel(3, "#general-chat", "General", route.circleId, 3, false)
                )

                val mockCategories = listOf(
                    ChannelCategory(
                        id = 1,
                        name = "General",
                        position = 1,
                        channels = mockChannels
                    )
                )

                ManageChannelsScreen(
                    circleId = route.circleId,
                    initialChannels = mockChannels,
                    initialCategories = mockCategories,
                    onNavigateBack = { mainNavController.popBackStack() },
                    onSave = { channels, categories ->
                        // TODO: Save to ViewModel/API
                        mainNavController.popBackStack()
                    }
                )
            }

            // Member List - Basic member list screen
            composable<Route.MemberList> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.MemberList>()

                // TODO: Get actual current user ID from preferences/ViewModel
                val currentUserId = 1

                MemberListScreen(
                    circleId = route.circleId,
                    circleName = route.circleName,
                    currentUserId = currentUserId,
                    onNavigateBack = { mainNavController.popBackStack() },
                    onViewProfile = { userId ->
                        navController.navigate(Route.ProfilePreview(userId))
                    }
                )
            }

            // Dashboard Member List - Admin dashboard for payment tracking
            composable<Route.DashboardMemberList> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.DashboardMemberList>()

                // TODO: Get actual current user ID from preferences/ViewModel
                val currentUserId = 1

                DashboardMemberListScreen(
                    circleId = route.circleId,
                    circleName = route.circleName,
                    currentUserId = currentUserId,
                    onNavigateBack = { mainNavController.popBackStack() },
                    onViewProfile = { userId ->
                        navController.navigate(Route.ProfilePreview(userId))
                    }
                )
            }

            // Circle Dues - Payment management screen
            composable<Route.CircleDues> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.CircleDues>()

                // TODO: Get actual current user ID from preferences/ViewModel
                val currentUserId = 1

                // Create a CircleData object with the route info
                // In production, you'd fetch this from ViewModel/API
                val circleData = CircleData(
                    id = route.circleId,
                    name = route.circleName,
                    industry = "",
                    memberCount = 0,
                    pricing = "",
                    description = "",
                    isPrivate = false,
                    profileImageUrl = "",
                    isModerator = false,
                    accessCode = null,
                    duesAmount = null,
                    hasStripeAccount = false
                )

                CircleDuesScreen(
                    circle = circleData,
                    userId = currentUserId,
                    onNavigateBack = { mainNavController.popBackStack() }
                )
            }

            // Main App Navigation
            composable<Route.More> {
                GrowthHubScreen(
                    onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                    onNavigateToMessages = { navController.navigate(Route.Messages) }
                )
            }

            // Settings tab - Settings screen
            composable<Route.Settings> {
                SettingsScreen(
                    onNavigateToProfile = { navController.navigate(Route.MyProfile) },
                    onNavigateToMessages = { navController.navigate(Route.Messages) },
                    onNavigateToBecomeMentor = { navController.navigate(Route.BecomeMentor) },
                    onNavigateToChangePassword = { navController.navigate(Route.ChangePassword) },
                    onNavigateToBlockedUsers = { navController.navigate(Route.BlockedUsers) },
                    onNavigateToDeleteAccount = { navController.navigate(Route.DeleteAccount) },
                    onNavigateToSuggestFeature = { navController.navigate(Route.SuggestFeature) },
                    onNavigateToReportProblem = { navController.navigate(Route.ReportProblem) },
                    onNavigateToTerms = { navController.navigate(Route.TermsOfService) },
                    onNavigateToPrivacy = { navController.navigate(Route.PrivacyPolicy) },
                    onNavigateToGuidelines = { navController.navigate(Route.CommunityGuidelines) },
                    onNavigateToTutorial = { navController.navigate(Route.Tutorial) },
                    onNavigateToSupport = { navController.navigate(Route.ContactSupport) },
                    onLogout = {
                        // Clear user data and navigate to login
                        // TODO: Clear SharedPreferences/DataStore
                        navController.navigate(Route.Page1) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Settings sub-pages
            composable<Route.BecomeMentor> {
                BecomeMentorPage(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.ChangePassword> {
                ChangePasswordPage(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.BlockedUsers> {
                GenericSettingsPage(
                    title = "Blocked Users",
                    description = "Manage users you've blocked. This feature is coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.DeleteAccount> {
                GenericSettingsPage(
                    title = "Delete Account",
                    description = "Permanently delete your account. This feature is coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.SuggestFeature> {
                GenericSettingsPage(
                    title = "Suggest a Feature",
                    description = "Share your ideas to improve Circl. This feature is coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.ReportProblem> {
                GenericSettingsPage(
                    title = "Report a Problem",
                    description = "Let us know about any issues. This feature is coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.TermsOfService> {
                GenericSettingsPage(
                    title = "Terms of Service",
                    description = "Read our terms of service. Content coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.PrivacyPolicy> {
                GenericSettingsPage(
                    title = "Privacy Policy",
                    description = "Read our privacy policy. Content coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.CommunityGuidelines> {
                GenericSettingsPage(
                    title = "Community Guidelines",
                    description = "Learn about our community standards. Content coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.ContactSupport> {
                GenericSettingsPage(
                    title = "Contact Support",
                    description = "Get help from our support team. This feature is coming soon.",
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        } // NavHost closes
        } // Scaffold content lambda closes

    // Tutorial overlay (shown on top of everything)
    com.fragne.circl_app.tutorial.ui.TutorialOverlay(
        tutorialManager = tutorialManager
    )
  } // Box closes
} // MainScreen function closes

