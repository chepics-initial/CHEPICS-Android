package com.chepics.chepics.feature.authentication.onetimecode

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chepics.chepics.feature.ButtonType
import com.chepics.chepics.feature.CommonProgressSpinner
import com.chepics.chepics.feature.RoundButton
import com.chepics.chepics.feature.authentication.HeaderView
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.utils.Constants

@Composable
fun OneTimeCodeScreen(navController: NavController, email: String, viewModel: OneTimeCodeViewModel = viewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current

    if (viewModel.isCompleted.value) {
        navController.navigate(Screens.PasswordScreen.name)
        viewModel.isCompleted.value = false
    }

    Box {
        Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                ) {
                    HeaderView(title = "認証コードを入力", description = "${email}に送信された${Constants.ONE_TIME_CODE_LENGTH}桁のコードを入力してください")
                }

                RoundButton(text = "次へ", isActive = viewModel.code.value.count() == 4, type = ButtonType.Fill) {
                    viewModel.onTapButton()
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OtpTextField(
                    otpText = viewModel.code.value,
                    onOtpTextChange = { value, otpInputFilled ->
                        viewModel.code.value = value
                    }
                ) {
                    keyboardController?.hide()
                    viewModel.onTapButton()
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {  }) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(
                            text = "コードを再送信",
                            color = Color.LightGray,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
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

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 4,
    onOtpTextChange: (String, Boolean) -> Unit,
    onDone: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        }),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    )
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Column(
        modifier = modifier.width(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            text = char,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Divider(
            modifier = Modifier.height(2.dp),
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}