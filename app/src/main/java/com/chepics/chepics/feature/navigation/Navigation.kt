package com.chepics.chepics.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chepics.chepics.feature.authentication.CompletionScreen
import com.chepics.chepics.feature.authentication.emailregistration.EmailRegistrationScreen
import com.chepics.chepics.feature.authentication.iconregistration.IconRegistrationScreen
import com.chepics.chepics.feature.authentication.login.LoginScreen
import com.chepics.chepics.feature.authentication.nameregistration.NameRegistrationScreen
import com.chepics.chepics.feature.authentication.onetimecode.OneTimeCodeScreen
import com.chepics.chepics.feature.authentication.passwordregistration.PasswordRegistrationScreen

@Composable
fun Navigation() {
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