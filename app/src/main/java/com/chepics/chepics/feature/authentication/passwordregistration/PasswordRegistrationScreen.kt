package com.chepics.chepics.feature.authentication.passwordregistration

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chepics.chepics.feature.ButtonType
import com.chepics.chepics.feature.CommonProgressSpinner
import com.chepics.chepics.feature.RoundButton
import com.chepics.chepics.ui.theme.ChepicsPrimary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chepics.chepics.feature.authentication.HeaderView
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.utils.Constants

@Composable
fun PasswordRegistrationScreen(navController: NavController, viewModel: PasswordRegistrationViewModel = viewModel()) {
    if (viewModel.isCompleted.value) {
        navController.navigate(Screens.NameRegistrationScreen.name)
        viewModel.isCompleted.value = false
    }

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp),
                ) {
                    HeaderView(title = "パスワード作成", description = "${Constants.PASSWORD_LENGTH}文字以上のパスワードを設定してください")

                    OutlinedTextField(
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        label = { Text(text = "${Constants.PASSWORD_LENGTH}文字以上の半角英数字") },
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "確認のため再度パスワードを入力してください",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = viewModel.confirmPassword.value,
                        onValueChange = { viewModel.confirmPassword.value = it },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        label = { Text(text = "${Constants.PASSWORD_LENGTH}文字以上の半角英数字") },
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
                }

                RoundButton(text = "次へ", isActive = viewModel.isActive(), type = ButtonType.Fill) {
                    viewModel.onTapButton()
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }

        if (viewModel.showAlertDialog.value) {
            AlertDialog(
                onDismissRequest = {  },
                title = { Text(text = "エラー") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showAlertDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}