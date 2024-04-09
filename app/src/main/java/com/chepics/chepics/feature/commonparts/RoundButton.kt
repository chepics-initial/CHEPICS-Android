package com.chepics.chepics.feature.commonparts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    text: String,
    isActive: Boolean = true,
    type: ButtonType,
    action: () -> Unit
) {
    when (type) {
        ButtonType.Fill -> {
            Button(
                onClick = action,
                modifier = modifier.fillMaxWidth(),
                enabled = isActive,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChepicsPrimary
                )
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        ButtonType.Border -> {
            Button(
                onClick = action,
                modifier = modifier.fillMaxWidth(),
                enabled = isActive,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(2.dp, ChepicsPrimary),
                elevation = null
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    color = ChepicsPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

enum class ButtonType {
    Fill,
    Border
}