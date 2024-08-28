package com.chepics.chepics.feature.commonparts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NetworkErrorDialog(onTapButton: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "通信エラー") },
        text = { Text(text = "インターネット環境を確認して、もう一度お試しください。") },
        confirmButton = {
            TextButton(onClick = { onTapButton() }) {
                Text(text = "OK")
            }
        }
    )
}