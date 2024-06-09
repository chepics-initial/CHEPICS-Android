package com.chepics.chepics.feature.navigation

import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.CommentNavType
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.SetNavType
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.TopicNavType
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.UserNavType
import com.chepics.chepics.feature.authentication.completion.CompletionScreen
import com.chepics.chepics.feature.authentication.emailregistration.EmailRegistrationScreen
import com.chepics.chepics.feature.authentication.iconregistration.IconRegistrationScreen
import com.chepics.chepics.feature.authentication.login.LoginScreen
import com.chepics.chepics.feature.authentication.nameregistration.NameRegistrationScreen
import com.chepics.chepics.feature.authentication.onetimecode.OneTimeCodeScreen
import com.chepics.chepics.feature.authentication.passwordregistration.PasswordRegistrationScreen
import com.chepics.chepics.feature.comment.CommentDetailScreen
import com.chepics.chepics.feature.createcomment.CreateCommentNavigationItem
import com.chepics.chepics.feature.createcomment.CreateCommentNavigationItemNavType
import com.chepics.chepics.feature.createcomment.CreateCommentScreen
import com.chepics.chepics.feature.createtopic.CreateTopicScreen
import com.chepics.chepics.feature.editprofile.EditProfileScreen
import com.chepics.chepics.feature.explore.result.ExploreResultScreen
import com.chepics.chepics.feature.explore.top.ExploreTopScreen
import com.chepics.chepics.feature.feed.FeedScreen
import com.chepics.chepics.feature.mypage.top.MyPageTopScreen
import com.chepics.chepics.feature.mypage.topiclist.MyPageTopicListScreen
import com.chepics.chepics.feature.profile.ProfileScreen
import com.chepics.chepics.feature.topic.comment.SetCommentScreen
import com.chepics.chepics.feature.topic.commentdetail.SetCommentDetailScreen
import com.chepics.chepics.feature.topic.createset.CreateSetScreen
import com.chepics.chepics.feature.topic.detail.TopicDetailScreen
import com.chepics.chepics.feature.topic.top.TopicTopScreen
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.google.gson.Gson

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginScreen.name) {
        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(Screens.EmailRegistrationScreen.name) {
            EmailRegistrationScreen(navController = navController)
        }

        composable(
            "${Screens.OneTimeCodeScreen.name}/{email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("email").let {
                OneTimeCodeScreen(navController = navController, email = it.toString())
            }
        }

        composable(Screens.PasswordScreen.name) {
            PasswordRegistrationScreen(navController = navController)
        }

        composable(Screens.NameRegistrationScreen.name) {
            NameRegistrationScreen(navController = navController)
        }

        composable(Screens.CompletionScreen.name) {
            CompletionScreen(navController = navController)
        }

        composable(Screens.IconRegistrationScreen.name) {
            IconRegistrationScreen()
        }
    }
}

@Composable
fun ServiceNavigation() {
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val showBottomNavigation = remember {
        mutableStateOf(true)
    }
    val currentTab: MutableState<BottomNavigationItem> = remember {
        mutableStateOf(tabItems.first())
    }

    Scaffold(
        bottomBar = {
            if (showBottomNavigation.value) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.height(40.dp)
                ) {
                    tabItems.forEach { item ->
                        val isSelected =
                            item.name == navBackStackEntry?.destination?.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentTab.value == item) return@NavigationBarItem
                                currentTab.value = item
                                rootNavController.navigate(item.name) {
                                    popUpTo(rootNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.name
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ChepicsPrimary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            rootNavController,
            modifier = Modifier.padding(it),
            startDestination = BottomNavigationType.Feed.name
        ) {
            composable(BottomNavigationType.Feed.name) {
                FeedNavHost(showBottomNavigation)
            }

            composable(BottomNavigationType.MyPage.name) {
                MyPageNavHost(showBottomNavigation)
            }
        }
    }
}

@Composable
fun FeedNavHost(showBottomNavigation: MutableState<Boolean>) {
    val feedNavController = rememberNavController()
    NavHost(feedNavController, startDestination = Screens.FeedScreen.name) {
        composable(Screens.FeedScreen.name) {
            FeedScreen(
                navController = feedNavController,
                showBottomNavigation = showBottomNavigation
            )
        }

        composable(
            Screens.CreateTopicScreen.name,
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) {
            CreateTopicScreen(
                navController = feedNavController,
                showBottomNavigation = showBottomNavigation
            )
        }

        composable(Screens.ExploreTopScreen.name) {
            ExploreTopScreen(navController = feedNavController)
        }

        composable(
            "${Screens.ExploreResultScreen.name}/{searchText}",
            arguments = listOf(navArgument("searchText") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("searchText")?.let {
                ExploreResultScreen(
                    navController = feedNavController,
                    searchText = it,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.ProfileScreen.name}/{user}",
            arguments = listOf(navArgument("user") {
                type = UserNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("user")?.let {
                Gson().fromJson(it, User::class.java)
            }?.let {
                ProfileScreen(
                    navController = feedNavController,
                    user = it,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.EditProfileScreen.name}/{user}",
            arguments = listOf(navArgument("user") {
                type = UserNavType()
            }),
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("user")?.let {
                Gson().fromJson(it, User::class.java)
            }?.let {
                EditProfileScreen(
                    navController = feedNavController,
                    showBottomNavigation = showBottomNavigation,
                    user = it
                )
            }
        }

        composable(
            "${Screens.CommentDetailScreen.name}/{comment}",
            arguments = listOf(navArgument("comment") {
                type = CommentNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("comment")?.let {
                Gson().fromJson(it, Comment::class.java)
            }?.let {
                CommentDetailScreen(
                    comment = it,
                    navController = feedNavController,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.TopicTopScreen.name}/{topic}",
            arguments = listOf(navArgument("topic") {
                type = TopicNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topic")?.let {
                Gson().fromJson(it, Topic::class.java)
            }?.let {
                TopicTopScreen(
                    navController = feedNavController,
                    topic = it,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.TopicDetailScreen.name}/{topic}",
            arguments = listOf(navArgument("topic") {
                type = TopicNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topic")?.let {
                Gson().fromJson(it, Topic::class.java)
            }?.let {
                TopicDetailScreen(
                    topic = it,
                    navController = feedNavController,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.CreateSetScreen.name}/{topicId}",
            arguments = listOf(navArgument("topicId") {
                type = NavType.StringType
            }),
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topicId")?.let { topicId ->
                val onBack =
                    feedNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("onBack")
                val completion =
                    feedNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("completion")
                onBack?.let {
                    completion?.let {
                        CreateSetScreen(
                            topicId = topicId,
                            navController = feedNavController,
                            showBottomNavigation = showBottomNavigation,
                            onBack = {
                                onBack()
                            },
                            completion = {
                                completion()
                            }
                        )
                    }
                }
            }
        }

        composable(
            "${Screens.SetCommentScreen.name}/{set}",
            arguments = listOf(navArgument("set") {
                type = SetNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("set")?.let { set ->
                Gson().fromJson(set, PickSet::class.java)
            }?.let { set ->
                val onBack =
                    feedNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("onBack")
                onBack?.let { onBack ->
                    SetCommentScreen(
                        set = set,
                        navController = feedNavController
                    ) {
                        onBack()
                    }
                }
            }
        }

        composable(
            "${Screens.SetCommentDetailScreen.name}/{set}/{comment}",
            arguments = listOf(navArgument("set") {
                type = SetNavType()
            },
                navArgument("comment") {
                    type = CommentNavType()
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("set")?.let { set ->
                Gson().fromJson(set, PickSet::class.java)
            }?.let { set ->
                backStackEntry.arguments?.getString("comment")?.let { comment ->
                    Gson().fromJson(comment, Comment::class.java)
                }?.let { comment ->
                    val onBack =
                        feedNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>(
                            "onBack"
                        )
                    onBack?.let { onBack ->
                        SetCommentDetailScreen(
                            set = set,
                            comment = comment,
                            navController = feedNavController
                        ) {
                            onBack()
                        }
                    }
                }
            }
        }

        composable(
            "${Screens.CreateCommentScreen.name}/{navigationItem}",
            arguments = listOf(navArgument("navigationItem") {
                type = CreateCommentNavigationItemNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("navigationItem")?.let { item ->
                Gson().fromJson(item, CreateCommentNavigationItem::class.java)
            }?.let { item ->
                CreateCommentScreen(
                    navController = feedNavController,
                    showBottomNavigation = showBottomNavigation,
                    navigationItem = item
                )
            }
        }
    }
}

@Composable
fun MyPageNavHost(showBottomNavigation: MutableState<Boolean>) {
    val myPageNavController = rememberNavController()
    NavHost(myPageNavController, startDestination = Screens.MyPageTopScreen.name) {
        composable(
            "${Screens.ProfileScreen.name}/{user}",
            arguments = listOf(navArgument("user") {
                type = UserNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("user")?.let {
                Gson().fromJson(it, User::class.java)
            }?.let {
                ProfileScreen(
                    navController = myPageNavController,
                    user = it,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(Screens.MyPageTopScreen.name) {
            MyPageTopScreen(navController = myPageNavController)
        }

        composable(
            "${Screens.EditProfileScreen.name}/{user}",
            arguments = listOf(navArgument("user") {
                type = UserNavType()
            }),
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("user")?.let {
                Gson().fromJson(it, User::class.java)
            }?.let {
                EditProfileScreen(
                    navController = myPageNavController,
                    showBottomNavigation = showBottomNavigation,
                    user = it
                )
            }
        }

        composable(Screens.MyPageTopicListScreen.name) {
            MyPageTopicListScreen(navController = myPageNavController)
        }

        composable(
            "${Screens.CommentDetailScreen.name}/{comment}",
            arguments = listOf(navArgument("comment") {
                type = CommentNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("comment")?.let {
                Gson().fromJson(it, Comment::class.java)
            }?.let {
                CommentDetailScreen(
                    comment = it,
                    navController = myPageNavController,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.TopicTopScreen.name}/{topic}",
            arguments = listOf(navArgument("topic") {
                type = TopicNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topic")?.let {
                Gson().fromJson(it, Topic::class.java)
            }?.let {
                TopicTopScreen(
                    navController = myPageNavController,
                    topic = it,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.TopicDetailScreen.name}/{topic}",
            arguments = listOf(navArgument("topic") {
                type = TopicNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topic")?.let {
                Gson().fromJson(it, Topic::class.java)
            }?.let {
                TopicDetailScreen(
                    topic = it,
                    navController = myPageNavController,
                    showBottomNavigation = showBottomNavigation
                )
            }
        }

        composable(
            "${Screens.CreateSetScreen.name}/{topicId}",
            arguments = listOf(navArgument("topicId") {
                type = NavType.StringType
            }),
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("topicId")?.let { topicId ->
                val onBack =
                    myPageNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("onBack")
                val completion =
                    myPageNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("completion")
                onBack?.let {
                    completion?.let {
                        CreateSetScreen(
                            topicId = topicId,
                            navController = myPageNavController,
                            showBottomNavigation = showBottomNavigation,
                            onBack = {
                                onBack()
                            },
                            completion = {
                                completion()
                            }
                        )
                    }
                }
            }
        }

        composable(
            "${Screens.SetCommentScreen.name}/{set}",
            arguments = listOf(navArgument("set") {
                type = SetNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("set")?.let {
                Gson().fromJson(it, PickSet::class.java)
            }?.let { set ->
                val onBack =
                    myPageNavController.previousBackStackEntry?.savedStateHandle?.get<() -> Unit>("onBack")
                onBack?.let { onBack ->
                    SetCommentScreen(
                        set = set,
                        navController = myPageNavController
                    ) {
                        onBack()
                    }
                }
            }
        }

        composable(
            "${Screens.CreateCommentScreen.name}/{navigationItem}",
            arguments = listOf(navArgument("navigationItem") {
                type = CreateCommentNavigationItemNavType()
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("navigationItem")?.let { item ->
                Gson().fromJson(item, CreateCommentNavigationItem::class.java)
            }?.let { item ->
                CreateCommentScreen(
                    navController = myPageNavController,
                    showBottomNavigation = showBottomNavigation,
                    navigationItem = item
                )
            }
        }
    }
}

data class BottomNavigationItem(
    val name: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val tabItems = listOf(
    BottomNavigationItem(
        name = BottomNavigationType.Feed.name,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        BottomNavigationType.MyPage.name,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)