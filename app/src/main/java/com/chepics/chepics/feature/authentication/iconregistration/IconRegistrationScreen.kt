package com.chepics.chepics.feature.authentication.iconregistration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chepics.chepics.feature.CommonProgressSpinner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chepics.chepics.feature.authentication.HeaderView

@Composable
fun IconRegistrationScreen(navController: NavController, viewModel: IconRegistrationViewModel = viewModel()) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), 
        ) {
            Column(modifier = Modifier.padding(vertical = 24.dp)) {
                HeaderView(title = "プロフィール画像設定", description = "プロフィール画像は後から編集することができます")
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }
    }
}