package com.chepics.chepics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.chepics.chepics.feature.navigation.AuthNavigation
import com.chepics.chepics.feature.navigation.ServiceNavigation
import com.chepics.chepics.feature.splash.SplashScreen
import com.chepics.chepics.ui.theme.CHEPICSTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        setContent {
            CHEPICSTheme {
                ChepicsApp(viewModel)
            }
        }
    }
}

@Composable
fun ChepicsApp(viewModel: MainActivityViewModel) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isSplashProgress.value) {
                SplashScreen(isSplashProgress = viewModel.isSplashProgress)
            } else if (viewModel.isLoggedIn.value) {
                ServiceNavigation()
            } else {
                AuthNavigation()
            }
        }
    }
}