package com.fragne.circl_app.ui.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Forum/Feed Screen
 * Manages posts, likes, comments, and user interactions
 */
class ForumViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForumUiState())
    val uiState: StateFlow<ForumUiState> = _uiState.asStateFlow()

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

        fetchPosts()
    }

    fun fetchPosts(filter: String = _uiState.value.selectedFilter) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // TODO: Replace with actual API call to Django backend
                // For now, use mock data
                delay(1000) // Simulate network delay

                // Generate timestamps relative to now for realistic "time ago" display
                val now = System.currentTimeMillis()
                val fiveMinAgo = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
                    timeZone = java.util.TimeZone.getTimeZone("UTC")
                }.format(java.util.Date(now - 5 * 60 * 1000))
                val oneHourAgo = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
                    timeZone = java.util.TimeZone.getTimeZone("UTC")
                }.format(java.util.Date(now - 60 * 60 * 1000))
                val threeDaysAgo = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
                    timeZone = java.util.TimeZone.getTimeZone("UTC")
                }.format(java.util.Date(now - 3 * 24 * 60 * 60 * 1000))

                val mockPosts = listOf(
                    ForumPostModel(
                        id = 1,
                        user = "Sarah Anderson",
                        userId = 1,
                        profileImage = null,
                        content = "Just closed our Series A! 🎉 Grateful for everyone who believed in our vision. Here's what I learned during the fundraising process...",
                        category = "Funding & Finance",
                        privacy = "Public",
                        createdAt = fiveMinAgo,
                        commentCount = 23,
                        likeCount = 156,
                        likedByUser = true,
                        isMentor = true
                    ),
                    ForumPostModel(
                        id = 2,
                        user = "David Chen",
                        userId = 2,
                        profileImage = null,
                        content = "Looking for a co-founder with technical background. Building in the FinTech space. DM me if interested!",
                        category = "Networking & Collaboration",
                        privacy = "Public",
                        createdAt = fiveMinAgo,
                        commentCount = 12,
                        likeCount = 45,
                        likedByUser = false,
                        isMentor = false
                    ),
                    ForumPostModel(
                        id = 3,
                        user = "Emily Rodriguez",
                        userId = 3,
                        profileImage = null,
                        content = "The biggest mistake I made in my first startup was not validating the market early enough. Don't make the same mistake!",
                        category = "Challenges & Insights",
                        privacy = "Public",
                        createdAt = oneHourAgo,
                        commentCount = 34,
                        likeCount = 289,
                        likedByUser = true,
                        isMentor = true
                    ),
                    ForumPostModel(
                        id = 4,
                        user = "Marcus Thompson",
                        userId = 4,
                        profileImage = null,
                        content = "Does anyone have experience with Google Ads for B2B SaaS? Our CAC is too high and I need to optimize our campaigns.",
                        category = "Growth & Marketing",
                        privacy = "Public",
                        createdAt = oneHourAgo,
                        commentCount = 18,
                        likeCount = 67,
                        likedByUser = false,
                        isMentor = false
                    ),
                    ForumPostModel(
                        id = 5,
                        user = "Jennifer Wu",
                        userId = 5,
                        profileImage = null,
                        content = "AI is transforming how we build products. Just implemented ChatGPT into our customer service - response time decreased by 80%! 🤖",
                        category = "Trends & Technology",
                        privacy = "Public",
                        createdAt = oneHourAgo,
                        commentCount = 45,
                        likeCount = 234,
                        likedByUser = true,
                        isMentor = true
                    ),
                    ForumPostModel(
                        id = 6,
                        user = "Alex Kumar",
                        userId = 6,
                        profileImage = null,
                        content = "Hosting a virtual workshop on 'Scaling from 0 to 100k users' next Tuesday. Limited spots available. Who's interested?",
                        category = "Skills & Development",
                        privacy = "Public",
                        createdAt = threeDaysAgo,
                        commentCount = 56,
                        likeCount = 178,
                        likedByUser = false,
                        isMentor = true
                    ),
                    ForumPostModel(
                        id = 7,
                        user = "Rachel Martinez",
                        userId = 7,
                        profileImage = null,
                        content = "Remote work is the future. We went fully remote 2 years ago and productivity increased by 40%. Happy to share our playbook!",
                        category = "Challenges & Insights",
                        privacy = "Public",
                        createdAt = threeDaysAgo,
                        commentCount = 89,
                        likeCount = 412,
                        likedByUser = false,
                        isMentor = false
                    ),
                    ForumPostModel(
                        id = 8,
                        user = "Tom Bradley",
                        userId = 8,
                        profileImage = null,
                        content = "Can someone recommend a good lawyer for startup incorporation? Looking for someone familiar with Delaware C-Corp structures.",
                        category = "Funding & Finance",
                        privacy = "Public",
                        createdAt = threeDaysAgo,
                        commentCount = 27,
                        likeCount = 93,
                        likedByUser = false,
                        isMentor = false
                    )
                )

                _uiState.update { it.copy(
                    posts = mockPosts,
                    isLoading = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Failed to load posts: ${e.message}"
                )}
            }
        }
    }

    fun fetchPostsWithTabSwitch(filter: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                isTabSwitchLoading = true,
                selectedFilter = filter,
                selectedPrivacy = if (filter == "public") "Public" else "My Network"
            )}

            try {
                delay(500)
                fetchPosts(filter)
            } finally {
                _uiState.update { it.copy(isTabSwitchLoading = false) }
            }
        }
    }

    fun updatePostContent(content: String) {
        _uiState.update { it.copy(postContent = content) }
    }

    fun updateSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSelectedPrivacy(privacy: String) {
        _uiState.update { it.copy(selectedPrivacy = privacy) }
    }

    fun submitPost() {
        val content = _uiState.value.postContent.trim()
        if (content.isEmpty()) return

        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call to Django backend
                delay(500)

                // Clear the input
                _uiState.update { it.copy(
                    postContent = "",
                    selectedCategory = "Category"
                )}

                // Refresh posts
                fetchPosts()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to submit post: ${e.message}"
                )}
            }
        }
    }

    fun toggleLike(post: ForumPostModel) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(200)

                // Update the post in the list
                _uiState.update { state ->
                    state.copy(
                        posts = state.posts.map { p ->
                            if (p.id == post.id) {
                                p.copy(
                                    likedByUser = !p.likedByUser,
                                    likeCount = if (p.likedByUser) p.likeCount - 1 else p.likeCount + 1
                                )
                            } else {
                                p
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to like post: ${e.message}"
                )}
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                delay(300)

                _uiState.update { state ->
                    state.copy(posts = state.posts.filter { it.id != postId })
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = "Failed to delete post: ${e.message}"
                )}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

