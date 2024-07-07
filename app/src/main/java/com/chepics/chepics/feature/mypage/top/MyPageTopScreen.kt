package com.chepics.chepics.feature.mypage.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.R
import com.chepics.chepics.feature.commonparts.IconScale
import com.chepics.chepics.feature.commonparts.UserIcon
import com.chepics.chepics.feature.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun MyPageTopScreen(navController: NavController, viewModel: MyPageTopViewModel = hiltViewModel()) {
    val showConfirmDialog = remember {
        mutableStateOf(false)
    }
    val isNavigationEnabled = remember {
        mutableStateOf(true)
    }

    if (!isNavigationEnabled.value) {
        LaunchedEffect(key1 = true) {
            delay(1000L)
            isNavigationEnabled.value = true
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.user.value?.let {
                            if (isNavigationEnabled.value) {
                                isNavigationEnabled.value = false
                                navController.navigate(Screens.ProfileScreen.name + "/${it}")
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserIcon(url = viewModel.uiModel.value?.imageUrl, scale = IconScale.PROFILE)

                    Spacer(modifier = Modifier.width(16.dp))

                    viewModel.uiModel.value?.let {
                        Column {
                            Text(
                                text = it.fullname,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "@${it.username}",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.arrow_forward_ios),
                    contentDescription = "arrow image",
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screens.MyPageTopicListScreen.name) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "参加中のセット一覧",
                    fontWeight = FontWeight.SemiBold
                )

                Image(
                    painter = painterResource(id = R.drawable.arrow_forward_ios),
                    contentDescription = "arrow image",
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = { showConfirmDialog.value = true }) {
                Text(
                    text = "ログアウト",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showConfirmDialog.value) {
            AlertDialog(onDismissRequest = {

            }, title = {
                Text(text = "ログアウトしますか？")
            }, dismissButton = {
                TextButton(onClick = { showConfirmDialog.value = false }) {
                    Text(text = "キャンセル")
                }
            }, confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                }) {
                    Text(text = "ログアウト")
                }
            })
        }
    }
}