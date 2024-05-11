package com.chepics.chepics.feature.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    user: User,
    viewModel: EditProfileViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val isEnabled = remember {
        mutableStateOf(true)
    }

    val iconImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.imageUri.value = uri
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        showBottomNavigation.value = false
        viewModel.onAppear(user)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        showBottomNavigation.value = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Box {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "dismiss button"
                        )
                    }

                    Text(
                        text = "プロフィール編集",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                iconImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                    ) {
                        if (viewModel.imageUri.value != null) {
                            AsyncImage(
                                model = viewModel.imageUri.value,
                                contentDescription = "selected icon",
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Surface(
                                modifier = Modifier
                                    .size(96.dp)
                                    .align(Alignment.Center),
                                color = Color.LightGray,
                                shape = CircleShape
                            ) {
                                Image(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "icon",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd),
                            color = ChepicsPrimary,
                            shape = CircleShape
                        ) {
                            Image(
                                imageVector = Icons.Default.Add,
                                contentDescription = "select",
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    UsernameView(viewModel = viewModel)
                    
                    FullnameView(viewModel = viewModel)
                    
                    BioView(viewModel = viewModel)
                }
                Column(modifier = Modifier.wrapContentHeight()) {
                    HorizontalDivider()

                    RoundButton(
                        modifier = Modifier.padding(16.dp),
                        text = "保存",
                        isActive = true,
                        type = ButtonType.Fill
                    ) {
                    }
                }
            }
            if (viewModel.isLoading.value) {
                CommonProgressSpinner()
            }
        }
    }
}

@Composable
fun UsernameView(viewModel: EditProfileViewModel) {
    Column {
        Text(
            text = "ユーザー名",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        TextField(
            value = viewModel.username.value,
            onValueChange = { viewModel.username.value = it },
            label = { Text(text = "ユーザー名を入力") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = ChepicsPrimary,
                focusedIndicatorColor = ChepicsPrimary,
                focusedLabelColor = ChepicsPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${viewModel.username.value.length}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " / ${Constants.NAME_COUNT}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun FullnameView(viewModel: EditProfileViewModel) {
    Column {
        Text(
            text = "表示名",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        TextField(
            value = viewModel.fullname.value,
            onValueChange = { viewModel.fullname.value = it },
            label = { Text(text = "表示名を入力") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = ChepicsPrimary,
                focusedIndicatorColor = ChepicsPrimary,
                focusedLabelColor = ChepicsPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${viewModel.fullname.value.length}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " / ${Constants.NAME_COUNT}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BioView(viewModel: EditProfileViewModel) {
    Column {
        Text(
            text = "自己紹介",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        TextField(
            value = viewModel.bio.value,
            onValueChange = { viewModel.bio.value = it },
            label = { Text(text = "自己紹介を入力") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = ChepicsPrimary,
                focusedIndicatorColor = ChepicsPrimary,
                focusedLabelColor = ChepicsPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${viewModel.bio.value.length}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = " / ${Constants.BIO_COUNT}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}