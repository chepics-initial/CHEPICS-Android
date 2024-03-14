package com.chepics.chepics.feature.authentication.onetimecode

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextRange
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
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@Composable
fun OneTimeCodeScreen(navController: NavController, viewModel: OneTimeCodeViewModel = viewModel()) {
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
                    Text(
                        text = "認証コードを入力",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "に送信された${Constants.ONE_TIME_CODE_COUNT}桁のコードを入力してください",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                RoundButton(text = "次へ", isActive = viewModel.code.value.count() == 4, type = ButtonType.Fill) {
                    viewModel.onTapButton()
                }
            }

            OtpTextField(
                modifier = Modifier.align(Alignment.Center),
                otpText = viewModel.code.value,
                onOtpTextChange = { value, otpInputFilled ->
                    viewModel.code.value = value
                }
            )

//            TextField(
//                value = viewModel.code.value,
//                onValueChange = { if (it.count() <= Constants.ONE_TIME_CODE_COUNT) viewModel.code.value = it },
//                modifier = Modifier
//                    .width(0.dp)
//                    .height(0.dp)
//                    .align(Alignment.Center)
//            )

//            Row(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .fillMaxWidth()
//            ) {
//                for (i in 0 ..Constants.ONE_TIME_CODE_COUNT) {
//                    Column(modifier = Modifier.fillMaxWidth()) {
//                        Text(text = viewModel.getCodeFromIndex(i).toString())
//                    }
//                }
//            }
        }

//        if (viewModel.isLoading.value) {
//            CommonProgressSpinner()
//        }
//
//        if (viewModel.showAlertDialog.value) {
//            AlertDialog(
//                onDismissRequest = {  },
//                title = { Text(text = "エラー") },
//                confirmButton = {
//                    TextButton(onClick = { viewModel.showAlertDialog.value = false }) {
//                        Text(text = "OK")
//                    }
//                }
//            )
//        }
    }
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 4,
    onOtpTextChange: (String, Boolean) -> Unit
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Column(
        modifier = Modifier.width(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            text = char,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Divider(
            modifier = Modifier.height(2.dp)
        )
    }
}