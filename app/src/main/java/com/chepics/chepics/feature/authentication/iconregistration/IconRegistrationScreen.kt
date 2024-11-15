package com.chepics.chepics.feature.authentication.iconregistration

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import coil.compose.AsyncImage
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.feature.authentication.HeaderView
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun IconRegistrationScreen(viewModel: IconRegistrationViewModel = hiltViewModel()) {
    val isEnabled = remember {
        mutableStateOf(true)
    }

    val iconImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        isEnabled.value = true
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.imageUri.value = uri
    }

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
            ) {
                HeaderView(
                    title = "プロフィール画像設定",
                    description = "プロフィール画像は後から編集することができます"
                )

                if (viewModel.imageUri.value != null) {
                    AsyncImage(
                        model = viewModel.imageUri.value,
                        contentDescription = "selected icon",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                if (isEnabled.value) {
                                    isEnabled.value = false
                                    iconImageLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                if (isEnabled.value) {
                                    isEnabled.value = false
                                    iconImageLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            }
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(160.dp)
                                .align(Alignment.Center),
                            color = Color.LightGray,
                            shape = CircleShape
                        ) {
                            Image(
                                imageVector = Icons.Default.Person,
                                contentDescription = "icon",
                                modifier = Modifier.size(72.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd),
                            color = ChepicsPrimary,
                            shape = CircleShape
                        ) {
                            Image(
                                imageVector = Icons.Default.Add,
                                contentDescription = "select",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                RoundButton(
                    text = "画像を登録",
                    isActive = viewModel.imageUri.value != null,
                    type = ButtonType.Fill
                ) {
                    viewModel.onTapRegisterButton()
                }

                Spacer(modifier = Modifier.height(16.dp))

                RoundButton(text = "スキップ", type = ButtonType.Border) {
                    viewModel.onTapSkipButton()
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }
    }
}