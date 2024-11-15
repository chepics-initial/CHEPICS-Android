package com.chepics.chepics.feature.authentication.nameregistration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.feature.authentication.HeaderView
import com.chepics.chepics.feature.commonparts.NetworkErrorDialog
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameRegistrationScreen(
    navController: NavController,
    viewModel: NameRegistrationViewModel = hiltViewModel()
) {
    if (viewModel.isCompleted.value) {
        navController.navigate(Screens.CompletionScreen.name) {
            popUpTo(0)
        }
        viewModel.isCompleted.value = false
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Image(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(vertical = 24.dp)) {
                        HeaderView(
                            title = "基本設定",
                            description = "ユーザー名と表示名は後から編集することができます"
                        )

                        Column {
                            Text(
                                text = "ユーザー名",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "半角英数字でユーザー名を設定してください",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "ユーザー名は一意である必要があります",
                                style = MaterialTheme.typography.bodySmall
                            )
                            OutlinedTextField(
                                value = viewModel.username.value,
                                onValueChange = { viewModel.username.value = it },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                maxLines = 1,
                                label = { Text(text = "ユーザー名を入力") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    cursorColor = ChepicsPrimary,
                                    focusedIndicatorColor = ChepicsPrimary,
                                    focusedLabelColor = ChepicsPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column {
                            Text(
                                text = "表示名",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "サービス内で表示される名前を設定してください",
                                style = MaterialTheme.typography.labelLarge
                            )
                            OutlinedTextField(
                                value = viewModel.fullname.value,
                                onValueChange = { viewModel.fullname.value = it },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                maxLines = 1,
                                label = { Text(text = "表示名を入力") },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    cursorColor = ChepicsPrimary,
                                    focusedIndicatorColor = ChepicsPrimary,
                                    focusedLabelColor = ChepicsPrimary
                                )
                            )
                        }
                    }

                    RoundButton(
                        text = "次へ",
                        isActive = viewModel.isActive(),
                        type = ButtonType.Fill
                    ) {
                        viewModel.onTapButton()
                    }
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }

        if (viewModel.showAlertDialog.value) {
            NetworkErrorDialog {
                viewModel.showAlertDialog.value = false
            }
        }

        if (viewModel.showUniqueAlertDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "このユーザー名はすでに使用されています") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showUniqueAlertDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}
