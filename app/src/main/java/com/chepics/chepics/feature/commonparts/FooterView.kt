package com.chepics.chepics.feature.commonparts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chepics.chepics.feature.common.FooterStatus

@Composable
fun FooterView(status: FooterStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        when (status) {
            FooterStatus.ALLFETCHED -> {
                Box(modifier = Modifier)
            }

            FooterStatus.LOADINGSTOPPED, FooterStatus.LOADINGSTARTED -> {
                CircularProgressIndicator()
            }

            FooterStatus.FAILURE -> {
                Text(text = "通信に失敗しました")
            }
        }
    }
}