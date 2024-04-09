package com.chepics.chepics.feature.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.feature.navigation.Screens

@Composable
fun CompletionScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "アカウント登録が完了しました！",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            RoundButton(text = "プロフィール画像を設定", type = ButtonType.Fill) {
                navController.navigate(Screens.IconRegistrationScreen.name)
            }

            Spacer(modifier = Modifier.height(16.dp))

            RoundButton(text = "スキップ", type = ButtonType.Border) {

            }
        }
    }
}