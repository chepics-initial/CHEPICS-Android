package com.chepics.chepics.feature.navigation

import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chepics.chepics.feature.authentication.CompletionScreen
import com.chepics.chepics.feature.authentication.emailregistration.EmailRegistrationScreen
import com.chepics.chepics.feature.authentication.iconregistration.IconRegistrationScreen
import com.chepics.chepics.feature.authentication.login.LoginScreen
import com.chepics.chepics.feature.authentication.nameregistration.NameRegistrationScreen
import com.chepics.chepics.feature.authentication.onetimecode.OneTimeCodeScreen
import com.chepics.chepics.feature.authentication.passwordregistration.PasswordRegistrationScreen
import com.chepics.chepics.feature.createtopic.CreateTopicScreen
import com.chepics.chepics.feature.feed.FeedScreen
import com.chepics.chepics.feature.profile.ProfileScreen
import com.chepics.chepics.ui.theme.ChepicsPrimary

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

        composable("${Screens.OneTimeCodeScreen.name}/{email}", arguments = listOf(navArgument("email") {
            type = NavType.StringType
        })) { backStackEntry ->
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
            IconRegistrationScreen(navController = navController)
        }
    }
}

@Composable
fun ServiceNavigation() {
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                tabItems.forEach { item ->
                    val isSelected =
                        item.name == navBackStackEntry?.destination?.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
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
                            selectedIconColor = Color.White,
                            indicatorColor = ChepicsPrimary
                        ))
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
                FeedNavHost()
            }

            composable(BottomNavigationType.CreateTopic.name) {
                CreateTopicNavHost()
            }

            composable(BottomNavigationType.MyPage.name) {
                MyPageNavHost()
            }
        }
    }
}

@Composable
fun FeedNavHost() {
    val feedNavController = rememberNavController()
    NavHost(feedNavController, startDestination = Screens.FeedScreen.name) {
        composable(Screens.FeedScreen.name) {
            FeedScreen(navController = feedNavController)
        }
    }
}

@Composable
fun CreateTopicNavHost() {
    val createTopicNavController = rememberNavController()
    NavHost(createTopicNavController, startDestination = Screens.CreateTopicScreen.name) {
        composable(
            Screens.CreateTopicScreen.name,
            enterTransition = {
                slideIn { fullSize -> IntOffset(0, fullSize.height) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(0, fullSize.height) }
            }
        ) {
            CreateTopicScreen(navController = createTopicNavController)
        }
    }
}

@Composable
fun MyPageNavHost() {
    val myPageNavController = rememberNavController()
    NavHost(myPageNavController, startDestination = Screens.ProfileScreen.name) {
        composable(Screens.ProfileScreen.name) {
            ProfileScreen(navController = myPageNavController)
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
        name = BottomNavigationType.CreateTopic.name,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    ),
    BottomNavigationItem(
        BottomNavigationType.MyPage.name,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)