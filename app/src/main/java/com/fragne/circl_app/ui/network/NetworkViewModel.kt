package com.fragne.circl_app.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Network Screen
 * Manages entrepreneurs, mentors, network connections, and friend requests
 */
class NetworkViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkUiState())
    val uiState: StateFlow<NetworkUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        // Load user info from preferences
        // TODO: Replace with actual DataStore/SharedPreferences
        _uiState.update { it.copy(
            userFirstName = "John",
            userProfileImageUrl = ""
        )}

        fetchAllNetworkData()
    }

    fun selectTab(tab: NetworkTab) {
        _uiState.update { it.copy(selectedTab = tab) }

        // Refresh data when switching tabs
        when (tab) {
            NetworkTab.ENTREPRENEURS -> fetchEntrepreneurs()
            NetworkTab.MENTORS -> fetchMentors()
            NetworkTab.MY_NETWORK -> fetchMyNetwork()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            fetchAllNetworkData()
            delay(500) // Smooth animation
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun fetchAllNetworkData() {
        fetchEntrepreneurs()
        fetchMentors()
        fetchMyNetwork()
        fetchPendingRequests()
    }

    private fun fetchEntrepreneurs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // TODO: Replace with actual API call
                delay(1000) // Simulate network delay

                val mockEntrepreneurs = listOf(
                    EntrepreneurProfile(
                        userId = 101,
                        name = "Sarah Anderson",
                        email = "sarah@startup.com",
                        profileImage = null,
                        businessName = "TechVenture Inc",
                        businessStage = "Series A",
                        businessIndustry = "FinTech",
                        tags = listOf("Blockchain", "Payments", "SaaS"),
                        bio = "Building the future of digital payments",
                        isVerified = true
                    ),
                    EntrepreneurProfile(
                        userId = 102,
                        name = "David Chen",
                        email = "david@innovation.com",
                        profileImage = null,
                        businessName = "AI Solutions",
                        businessStage = "Seed Stage",
                        businessIndustry = "Artificial Intelligence",
                        tags = listOf("AI", "Machine Learning", "B2B"),
                        bio = "Democratizing AI for small businesses",
                        isVerified = false
                    ),
                    EntrepreneurProfile(
                        userId = 103,
                        name = "Emily Rodriguez",
                        email = "emily@ecotech.com",
                        profileImage = null,
                        businessName = "EcoTech Innovations",
                        businessStage = "Growth Stage",
                        businessIndustry = "Clean Energy",
                        tags = listOf("Sustainability", "Green Tech", "Climate"),
                        bio = "Sustainable solutions for a better tomorrow",
                        isVerified = true
                    ),
                    EntrepreneurProfile(
                        userId = 104,
                        name = "Michael Torres",
                        email = "michael@healthplus.com",
                        profileImage = null,
                        businessName = "HealthPlus",
                        businessStage = "Pre-Seed",
                        businessIndustry = "HealthTech",
                        tags = listOf("Healthcare", "Telemedicine", "Mobile"),
                        bio = "Accessible healthcare for everyone",
                        isVerified = false
                    ),
                    EntrepreneurProfile(
                        userId = 105,
                        name = "Lisa Zhang",
                        email = "lisa@edtech.com",
                        profileImage = null,
                        businessName = "EduPro",
                        businessStage = "Series B",
                        businessIndustry = "EdTech",
                        tags = listOf("Education", "E-Learning", "Kids"),
                        bio = "Transforming education through technology",
                        isVerified = true
                    )
                )

                _uiState.update { it.copy(
                    entrepreneurs = mockEntrepreneurs,
                    isLoading = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Failed to load entrepreneurs: ${e.message}"
                )}
            }
        }
    }

    private fun fetchMentors() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(1000) // Simulate network delay

                val mockMentors = listOf(
                    MentorProfile(
                        userId = 201,
                        name = "Dr. James Wilson",
                        email = "james@mentor.com",
                        profileImage = null,
                        expertise = listOf("Startup Strategy", "Product Development", "Fundraising"),
                        experience = "20+ years",
                        company = "Tech Giants Inc",
                        title = "Chief Innovation Officer",
                        mentorshipAreas = listOf("Business Strategy", "Product Market Fit", "Scaling"),
                        bio = "Former founder, now helping entrepreneurs succeed",
                        isVerified = true,
                        rating = 4.8f,
                        sessionsCompleted = 156
                    ),
                    MentorProfile(
                        userId = 202,
                        name = "Maria Garcia",
                        email = "maria@mentor.com",
                        profileImage = null,
                        expertise = listOf("Marketing", "Growth Hacking", "Brand Strategy"),
                        experience = "15+ years",
                        company = "Marketing Pro",
                        title = "VP of Marketing",
                        mentorshipAreas = listOf("Digital Marketing", "Customer Acquisition", "Branding"),
                        bio = "Growth marketing expert passionate about helping startups scale",
                        isVerified = true,
                        rating = 4.9f,
                        sessionsCompleted = 203
                    ),
                    MentorProfile(
                        userId = 203,
                        name = "Robert Taylor",
                        email = "robert@mentor.com",
                        profileImage = null,
                        expertise = listOf("Finance", "VC Relations", "Exit Strategy"),
                        experience = "25+ years",
                        company = "Investment Partners",
                        title = "Managing Partner",
                        mentorshipAreas = listOf("Fundraising", "Financial Planning", "M&A"),
                        bio = "Venture capitalist with successful exits and deep network",
                        isVerified = true,
                        rating = 4.7f,
                        sessionsCompleted = 89
                    )
                )

                _uiState.update { it.copy(mentors = mockMentors) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to load mentors: ${e.message}"
                )}
            }
        }
    }

    private fun fetchMyNetwork() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(1000) // Simulate network delay

                val mockConnections = listOf(
                    NetworkConnection(
                        userId = 301,
                        name = "Alex Kumar",
                        email = "alex@business.com",
                        profileImage = null,
                        connectionType = "friend",
                        connectedSince = "2024-01-15T10:30:00Z",
                        status = "active",
                        lastMessageTime = "2024-12-20T15:45:00Z",
                        unreadCount = 2,
                        company = "StartupHub",
                        title = "Co-Founder",
                        isOnline = true
                    ),
                    NetworkConnection(
                        userId = 302,
                        name = "Jennifer Lee",
                        email = "jennifer@tech.com",
                        profileImage = null,
                        connectionType = "friend",
                        connectedSince = "2024-02-20T14:20:00Z",
                        status = "active",
                        lastMessageTime = "2024-12-21T09:30:00Z",
                        unreadCount = 0,
                        company = "Innovation Labs",
                        title = "Product Manager",
                        isOnline = false
                    ),
                    NetworkConnection(
                        userId = 303,
                        name = "Marcus Johnson",
                        email = "marcus@venture.com",
                        profileImage = null,
                        connectionType = "mentor",
                        connectedSince = "2024-03-10T11:00:00Z",
                        status = "active",
                        lastMessageTime = "2024-12-19T16:20:00Z",
                        unreadCount = 1,
                        company = "Venture Capital Partners",
                        title = "Senior Partner",
                        isOnline = true
                    )
                )

                _uiState.update { it.copy(
                    myNetwork = mockConnections,
                    connectionCount = mockConnections.size,
                    activeCount = mockConnections.count { it.status == "active" }
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to load network: ${e.message}"
                )}
            }
        }
    }

    private fun fetchPendingRequests() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(1000) // Simulate network delay

                val mockRequests = listOf(
                    FriendRequest(
                        requestId = 1,
                        senderId = 401,
                        senderName = "Sophie Martinez",
                        senderEmail = "sophie@startup.com",
                        senderProfileImage = null,
                        message = "Hi! I'd love to connect and learn from your experience.",
                        createdAt = "2024-12-22T08:30:00Z",
                        status = "pending"
                    ),
                    FriendRequest(
                        requestId = 2,
                        senderId = 402,
                        senderName = "Daniel Park",
                        senderEmail = "daniel@company.com",
                        senderProfileImage = null,
                        message = "Let's collaborate on future projects!",
                        createdAt = "2024-12-21T16:45:00Z",
                        status = "pending"
                    )
                )

                _uiState.update { it.copy(pendingRequests = mockRequests) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to load requests: ${e.message}"
                )}
            }
        }
    }

    fun sendConnectionRequest(userId: Int, email: String) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(500)

                // Add to declined list temporarily (will be managed by backend)
                _uiState.update {
                    it.copy(declinedUserIds = it.declinedUserIds + userId)
                }

                // Show success message
                _uiState.update { it.copy(
                    errorMessage = "Connection request sent!"
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to send request: ${e.message}"
                )}
            }
        }
    }

    fun acceptFriendRequest(requestId: Int, senderId: Int) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(500)

                // Remove from pending requests
                _uiState.update { state ->
                    state.copy(
                        pendingRequests = state.pendingRequests.filter { it.requestId != requestId }
                    )
                }

                // Refresh network to show new connection
                fetchMyNetwork()

                _uiState.update { it.copy(
                    errorMessage = "Connection accepted!"
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to accept request: ${e.message}"
                )}
            }
        }
    }

    fun declineFriendRequest(requestId: Int) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(500)

                // Remove from pending requests
                _uiState.update { state ->
                    state.copy(
                        pendingRequests = state.pendingRequests.filter { it.requestId != requestId }
                    )
                }

                _uiState.update { it.copy(
                    errorMessage = "Request declined"
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to decline request: ${e.message}"
                )}
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

