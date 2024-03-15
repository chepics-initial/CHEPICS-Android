package com.chepics.chepics.feature.authentication.emailregistration

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chepics.chepics.feature.ButtonType
import com.chepics.chepics.feature.CommonProgressSpinner
import com.chepics.chepics.feature.RoundButton
import com.chepics.chepics.feature.authentication.HeaderView
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun EmailRegistrationScreen(navController: NavController, viewModel: EmailRegistrationViewModel = viewModel()) {
    if (viewModel.isCompleted.value) {
        navController.navigate(Screens.OneTimeCodeScreen.name + "/${viewModel.email.value}")
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
                    HeaderView(title = "メールアドレス登録", description = "ログイン時に使用するメールアドレスを入力してください")

                    OutlinedTextField(
                        value = viewModel.email.value,
                        onValueChange = { viewModel.email.value = it },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        label = { Text(text = "メールアドレスを入力")},
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

                RoundButton(text = "次へ", isActive = viewModel.email.value.isNotEmpty(), type = ButtonType.Fill) {
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