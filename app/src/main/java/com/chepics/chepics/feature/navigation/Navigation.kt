package com.chepics.chepics.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chepics.chepics.feature.authentication.emailregistration.EmailRegistrationScreen
import com.chepics.chepics.feature.authentication.login.LoginScreen
import com.chepics.chepics.feature.authentication.onetimecode.OneTimeCodeScreen

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

        composable(Screens.OneTimeCodeScreen.name) {
            OneTimeCodeScreen(navController = navController)
        }
    }
}