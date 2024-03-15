package com.chepics.chepics.feature.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chepics.chepics.utils.Constants

@Composable
fun HeaderView(
    title: String,
    description: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(16.dp))
}