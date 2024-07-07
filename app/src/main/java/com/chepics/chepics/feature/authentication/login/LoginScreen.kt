package com.chepics.chepics.feature.authentication.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = { viewModel.email.value = it },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    label = { Text(text = "メールアドレスを入力") },
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

                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.password.value = it },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    label = { Text(text = "パスワードを入力") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        cursorColor = ChepicsPrimary,
                        focusedIndicatorColor = ChepicsPrimary,
                        focusedLabelColor = ChepicsPrimary
                    )
                )

                Spacer(modifier = Modifier.height(150.dp))
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RoundButton(
                    text = "ログイン",
                    isActive = viewModel.loginButtonIsActive(),
                    type = ButtonType.Fill
                ) {
                    viewModel.onTapLoginButton()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "アカウントを持っていない場合",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                RoundButton(text = "新規登録", type = ButtonType.Border) {
                    navController.navigate(Screens.EmailRegistrationScreen.name)
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }

        if (viewModel.showAlertDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "通信エラー") },
                text = { Text(text = "インターネット環境を確認して、もう一度お試しください。") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showAlertDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }

        if (viewModel.showInvalidAlertDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "ログインできませんでした") },
                text = { Text(text = "メールアドレスとパスワードが正しいことを確認してください") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showInvalidAlertDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}