package com.fragne.circl_app.tutorial.content

import androidx.compose.ui.geometry.Rect
import com.fragne.circl_app.tutorial.models.TutorialFlow
import com.fragne.circl_app.tutorial.models.TutorialStep
import com.fragne.circl_app.tutorial.models.UserType

/**
 * Tutorial Content Factory
 * Creates personalized tutorial flows for each user type
 */
class TutorialContentFactory {

    // MARK: - Entrepreneur Tutorial Flow

    fun createEntrepreneurTutorial(): TutorialFlow {
        val steps = listOf(
            // 1. Welcome & Overview
            TutorialStep(
                title = "Welcome to Circl, Entrepreneur!",
                description = "Let's show you how Circl can accelerate your entrepreneurial journey",
                targetView = "main_navigation",
                message = "As an entrepreneur, you need tools to find co-founders, investors, mentors, and grow your network. Circl is designed specifically for ambitious founders like you.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                duration = null,
                isInteractive = false
            ),

            // 2. Home Feed - Bulletin Board
            TutorialStep(
                title = "Your Entrepreneurial Bulletin Board",
                description = "Share your updates and see what others in the community are posting",
                targetView = "home_tab",
                message = "Think of this as your community bulletin board where entrepreneurs share wins, challenges, insights, and opportunities. Post your own updates, celebrate milestones, and engage with fellow founders' content to build meaningful connections.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                duration = null,
                isInteractive = false
            ),

            // 3. Network Tab - Co-founder & Mentor Finding
            TutorialStep(
                title = "Find Co-Founders & Mentors",
                description = "Connect with potential co-founders, experienced mentors, and strategic partners",
                targetView = "network_tab",
                message = "This is your most powerful tool as an entrepreneur. Search for co-founders with complementary skills, experienced mentors who've been where you're going, and industry experts who can provide valuable guidance for your startup journey.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                duration = null,
                isInteractive = true
            ),

            // 4. Business Operations Hub
            TutorialStep(
                title = "Run Your Business Through Circles",
                description = "Use Circles as your business management and collaboration platform",
                targetView = "circles_tab",
                message = "Create a circle to run your business operations. Manage tasks, track KPIs, centralize team communication, and coordinate with co-founders. It's your startup's command center in one organized space.",
                navigationDestination = null,
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                duration = null,
                isInteractive = false
            ),

            // 5. Circles - Industry Communities
            TutorialStep(
                title = "Join Entrepreneurial Circles",
                description = "Connect with like-minded founders and industry-specific groups",
                targetView = "circles_tab",
                message = "Join circles based on your industry, business model, or stage of growth. Share challenges, celebrate wins, and learn from other entrepreneurs facing similar journeys.",
                navigationDestination = null,
                highlightRect = Rect(160f, 150f, 410f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                duration = null,
                isInteractive = false
            ),

            // 6. Advanced Circles Usage
            TutorialStep(
                title = "Create or Join Strategic Circles",
                description = "Build communities around your startup or join investor/advisor groups",
                targetView = "circles_tab",
                message = "Create a circle for your startup team and advisors, or join exclusive investor networks and accelerator groups. Use circles strategically to build your startup's ecosystem.",
                navigationDestination = "PageBusinessProfile",
                highlightRect = Rect(100f, 200f, 380f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.LEADING,
                duration = null,
                isInteractive = false
            ),

            // 7. Business Profile Creation
            TutorialStep(
                title = "Showcase Your Venture",
                description = "Create a compelling business profile to attract investors and co-founders",
                targetView = "business_profile_tab",
                message = "Your business profile is crucial for attracting investors and co-founders. Share your startup's mission, traction, funding needs, and what roles you're looking to fill.",
                navigationDestination = "PageEntrepreneurResources",
                highlightRect = Rect(30f, 120f, 370f, 470f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                duration = null,
                isInteractive = false
            ),

            // 8. Professional Services
            TutorialStep(
                title = "Access Startup Services",
                description = "Find lawyers, accountants, and other professionals for your business",
                targetView = "professional_services",
                message = "Access pre-vetted professionals offering startup-specific services like incorporation, accounting, marketing, and legal advice. Many offer special rates for early-stage companies.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(50f, 200f, 350f, 400f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                duration = null,
                isInteractive = false
            ),

            // 9. Collaboration Hub
            TutorialStep(
                title = "Conversate, Collaborate, Pass Networks, and Sell",
                description = "Use messages for deep connections and business opportunities",
                targetView = "messages_tab",
                message = "Use messages to have meaningful conversations with mentors, collaborate with co-founders and team members, pass valuable network connections to others, and engage with potential clients or customers. This is where relationships turn into business opportunities.",
                navigationDestination = "PageForum",
                highlightRect = Rect(60f, 100f, 340f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TRAILING,
                duration = null,
                isInteractive = false
            ),

            // 10. Success Strategies
            TutorialStep(
                title = "Entrepreneur Success Tips",
                description = "Make the most of Circl for your startup journey",
                targetView = "success_tips",
                message = "Pro tips: Update your business profile regularly, engage authentically in circles, be specific about what you're looking for, and always follow up on connections. Consistency builds trust!",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                duration = null,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.ENTREPRENEUR,
            title = "Entrepreneur's Guide to Circl",
            description = "Learn how to leverage Circl to find co-founders, investors, and grow your startup",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 10 * 60 * 1000L, // 10 minutes in milliseconds
            isRequired = true
        )
    }

    // MARK: - Student Tutorial Flow

    fun createStudentTutorial(): TutorialFlow {
        val steps = listOf(
            TutorialStep(
                title = "Welcome to Circl, Student!",
                description = "Let's show you how to make the most of your learning journey",
                targetView = "main_navigation",
                message = "As a student, Circl connects you with experienced entrepreneurs, mentors, and peers. Learn from those who've achieved what you're working toward.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            ),

            TutorialStep(
                title = "Your Learning Feed",
                description = "Discover insights and lessons from experienced professionals",
                targetView = "home_tab",
                message = "See posts from entrepreneurs sharing real-world lessons, industry insights, and career advice. Learn from their successes and mistakes.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Find Mentors & Industry Professionals",
                description = "Connect with people who can guide your career path",
                targetView = "network_tab",
                message = "Search for mentors in your field of interest, connect with professionals at companies you admire, and build relationships that can shape your career.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = true
            ),

            TutorialStep(
                title = "Join Student & Learning Circles",
                description = "Connect with other students and study groups",
                targetView = "circles_tab",
                message = "Join circles focused on your field of study, participate in peer learning groups, and connect with students from other universities.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Direct Mentorship Conversations",
                description = "Message mentors and build meaningful connections",
                targetView = "messages_tab",
                message = "Reach out to mentors for advice, ask questions about career paths, and build relationships that extend beyond your time in school.",
                navigationDestination = "PageForum",
                highlightRect = Rect(60f, 100f, 340f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TRAILING,
                isInteractive = false
            ),

            TutorialStep(
                title = "Student Success Tips",
                description = "Make the most of your Circl experience",
                targetView = "success_tips",
                message = "Pro tips: Be curious and ask thoughtful questions, share your own learning journey, be respectful of mentors' time, and always follow up with gratitude.",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.STUDENT,
            title = "Student's Guide to Circl",
            description = "Learn how to find mentors and accelerate your learning",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 8 * 60 * 1000L,
            isRequired = true
        )
    }

    // MARK: - Student Entrepreneur Tutorial Flow

    fun createStudentEntrepreneurTutorial(): TutorialFlow {
        // Hybrid of student and entrepreneur
        val steps = listOf(
            TutorialStep(
                title = "Welcome, Student Entrepreneur!",
                description = "You're building while learning - let's show you how Circl can help",
                targetView = "main_navigation",
                message = "As a student entrepreneur, you have unique needs - finding co-founders, learning from experienced founders, and balancing school with building. Circl is here to support both sides of your journey.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            ),

            TutorialStep(
                title = "Your Dual-Purpose Feed",
                description = "Learn and share as both student and founder",
                targetView = "home_tab",
                message = "See posts from both experienced entrepreneurs and fellow student founders. Learn from their journeys and share your own progress.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Find Co-Founders & Mentors",
                description = "Connect with fellow student entrepreneurs and experienced mentors",
                targetView = "network_tab",
                message = "Search for potential co-founders among other students, find mentors who've been student entrepreneurs, and connect with investors interested in student startups.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = true
            ),

            TutorialStep(
                title = "Manage Your Startup",
                description = "Use Circles to organize your student venture",
                targetView = "circles_tab",
                message = "Create a circle for your startup team, join student entrepreneur communities, and participate in university innovation circles.",
                navigationDestination = "PageBusinessProfile",
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Showcase Your Student Venture",
                description = "Create a profile for your student startup",
                targetView = "business_profile_tab",
                message = "Share your student startup's mission, what you're building, and what kind of support you're looking for - whether it's co-founders, mentors, or early customers.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(30f, 120f, 370f, 470f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = false
            ),

            TutorialStep(
                title = "Student Entrepreneur Success Tips",
                description = "Balance learning and building effectively",
                targetView = "success_tips",
                message = "Pro tips: Leverage your university resources, connect with other student founders, don't be afraid to ask for help, and remember that being a student entrepreneur is a unique advantage - you have time to experiment and learn!",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.STUDENT_ENTREPRENEUR,
            title = "Student Entrepreneur's Guide to Circl",
            description = "Learn how to build your startup while excelling in school",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 9 * 60 * 1000L,
            isRequired = true
        )
    }

    // MARK: - Mentor Tutorial Flow

    fun createMentorTutorial(): TutorialFlow {
        val steps = listOf(
            TutorialStep(
                title = "Welcome, Mentor!",
                description = "Your experience can change lives - let's show you how to share it",
                targetView = "main_navigation",
                message = "As a mentor, you have valuable knowledge and experience to share. Circl makes it easy to connect with aspiring entrepreneurs and students who need your guidance.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            ),

            TutorialStep(
                title = "Share Your Knowledge",
                description = "Post insights, lessons, and advice for the community",
                targetView = "home_tab",
                message = "Share posts about lessons you've learned, industry insights, career advice, and practical tips. Your experience is valuable to many people here.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Find Mentees",
                description = "Connect with entrepreneurs and students who need your guidance",
                targetView = "network_tab",
                message = "Browse profiles of entrepreneurs and students looking for mentors. Filter by industry, experience level, and specific needs to find great matches.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = true
            ),

            TutorialStep(
                title = "Lead Mentor Circles",
                description = "Create or join circles focused on mentorship and learning",
                targetView = "circles_tab",
                message = "Create circles around your areas of expertise, lead group mentorship sessions, and participate in industry-specific knowledge sharing.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "One-on-One Mentorship",
                description = "Have meaningful conversations with your mentees",
                targetView = "messages_tab",
                message = "Use messages for deeper mentorship conversations, provide personalized advice, and build lasting relationships with those you're guiding.",
                navigationDestination = "PageForum",
                highlightRect = Rect(60f, 100f, 340f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TRAILING,
                isInteractive = false
            ),

            TutorialStep(
                title = "Mentor Success Tips",
                description = "Make the biggest impact as a mentor",
                targetView = "success_tips",
                message = "Pro tips: Share specific, actionable advice, be encouraging but honest, make yourself available regularly, and remember that mentorship is a two-way street - you'll learn too!",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.MENTOR,
            title = "Mentor's Guide to Circl",
            description = "Learn how to share your knowledge and guide the next generation",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 8 * 60 * 1000L,
            isRequired = true
        )
    }

    // MARK: - Investor Tutorial Flow

    fun createInvestorTutorial(): TutorialFlow {
        val steps = listOf(
            TutorialStep(
                title = "Welcome, Investor!",
                description = "Discover your next investment opportunity",
                targetView = "main_navigation",
                message = "As an investor, Circl gives you direct access to founders, deal flow, and startup communities. Find investment opportunities and connect with entrepreneurs before everyone else.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            ),

            TutorialStep(
                title = "Discover Deal Flow",
                description = "See what entrepreneurs are building and sharing",
                targetView = "home_tab",
                message = "The feed shows you real-time updates from founders - product launches, traction milestones, and fundraising announcements. Spot opportunities early.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Find Investment Opportunities",
                description = "Search for startups that match your investment thesis",
                targetView = "network_tab",
                message = "Browse entrepreneur profiles, filter by industry and stage, review business profiles, and identify startups seeking investment.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = true
            ),

            TutorialStep(
                title = "Join Investor Circles",
                description = "Network with other investors and syndicate deals",
                targetView = "circles_tab",
                message = "Join investor circles to share deal flow, co-invest with other angels or VCs, and participate in industry-specific investment groups.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Direct Founder Access",
                description = "Message founders directly to learn about their ventures",
                targetView = "messages_tab",
                message = "Have direct conversations with founders, ask detailed questions about their business, and build relationships before making investment decisions.",
                navigationDestination = "PageForum",
                highlightRect = Rect(60f, 100f, 340f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TRAILING,
                isInteractive = false
            ),

            TutorialStep(
                title = "Investor Success Tips",
                description = "Make the most of your investor experience",
                targetView = "success_tips",
                message = "Pro tips: Be clear about your investment criteria, provide value beyond capital, build relationships before deals, and engage authentically with the community.",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.INVESTOR,
            title = "Investor's Guide to Circl",
            description = "Learn how to discover and connect with investment opportunities",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 8 * 60 * 1000L,
            isRequired = true
        )
    }

    // MARK: - Community Builder Tutorial Flow

    fun createCommunityBuilderTutorial(): TutorialFlow {
        val steps = listOf(
            TutorialStep(
                title = "Welcome to Circl!",
                description = "Let's show you around the community",
                targetView = "main_navigation",
                message = "Circl is a community of entrepreneurs, students, mentors, and innovators. Let's show you how to connect and engage with this amazing network.",
                navigationDestination = "PageForum",
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            ),

            TutorialStep(
                title = "Your Community Feed",
                description = "See what the community is sharing and discussing",
                targetView = "home_tab",
                message = "This is your window into the community. See updates, achievements, questions, and opportunities from members across the network.",
                navigationDestination = "PageUnifiedNetworking",
                highlightRect = Rect(50f, 100f, 350f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Discover Your Network",
                description = "Find and connect with interesting people",
                targetView = "network_tab",
                message = "Browse profiles, discover people with shared interests, and build your professional network one authentic connection at a time.",
                navigationDestination = "PageCircles",
                highlightRect = Rect(80f, 150f, 330f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TOP,
                isInteractive = true
            ),

            TutorialStep(
                title = "Join Circles",
                description = "Find your communities within the community",
                targetView = "circles_tab",
                message = "Circles are focused communities around industries, interests, or goals. Join circles that align with your interests and participate actively.",
                navigationDestination = "PageMessages",
                highlightRect = Rect(50f, 100f, 350f, 450f),
                tooltipAlignment = TutorialStep.TooltipAlignment.BOTTOM,
                isInteractive = false
            ),

            TutorialStep(
                title = "Connect & Collaborate",
                description = "Have meaningful conversations with your connections",
                targetView = "messages_tab",
                message = "Use messages to deepen relationships, collaborate on ideas, share resources, and build genuine connections.",
                navigationDestination = "PageForum",
                highlightRect = Rect(60f, 100f, 340f, 500f),
                tooltipAlignment = TutorialStep.TooltipAlignment.TRAILING,
                isInteractive = false
            ),

            TutorialStep(
                title = "Community Success Tips",
                description = "Make the most of your Circl experience",
                targetView = "success_tips",
                message = "Pro tips: Engage authentically, give before you ask, be supportive of others' journeys, and remember that every connection is an opportunity to learn and grow.",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )

        return TutorialFlow(
            userType = UserType.COMMUNITY_BUILDER,
            title = "Welcome to Circl",
            description = "Learn how to connect and engage with the community",
            steps = steps + createTutorialAccessStep() + createFinalCommunityStep(),
            estimatedDuration = 7 * 60 * 1000L,
            isRequired = true
        )
    }

    // MARK: - Common Steps

    /**
     * Tutorial access step - shown to all user types
     */
    private fun createTutorialAccessStep(): List<TutorialStep> {
        return listOf(
            TutorialStep(
                title = "Access This Tutorial Anytime",
                description = "You can always revisit this tutorial from Settings",
                targetView = "tutorial_access",
                message = "If you ever need a refresher, go to Settings > Tutorial & Help to restart this tutorial or explore different user type tutorials. We're here to help you succeed!",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )
    }

    /**
     * Final community welcome step - shown to all user types
     */
    private fun createFinalCommunityStep(): List<TutorialStep> {
        return listOf(
            TutorialStep(
                title = "Welcome to the Circl Community!",
                description = "You're all set to start your journey",
                targetView = "community_welcome",
                message = "You've completed the tutorial! Now it's time to dive in, make connections, and start building. Remember, success on Circl comes from authentic engagement and giving value to others. Welcome to the community!",
                navigationDestination = null,
                highlightRect = null,
                tooltipAlignment = TutorialStep.TooltipAlignment.CENTER,
                isInteractive = false
            )
        )
    }
}

