package com.chepics.chepics.feature.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chepics.chepics.R

@Composable
fun SplashScreen(isSplashProgress: MutableState<Boolean>) {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chepics_splash))
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        isPlaying = isSplashProgress.value
    )
    LaunchedEffect(key1 = progress.value) {
        if (progress.value == 0f) {
            isSplashProgress.value = true
        }

        if (progress.value == 1f) {
            isSplashProgress.value = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition.value,
            progress = { progress.value }
        )
    }
}