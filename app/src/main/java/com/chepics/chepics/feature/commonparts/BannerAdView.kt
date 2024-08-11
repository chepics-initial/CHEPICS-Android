package com.chepics.chepics.feature.commonparts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView(modifier: Modifier, adID: String) {
    Column(modifier) {
        Spacer(modifier = Modifier.size(24.dp))

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.MEDIUM_RECTANGLE)
                    adUnitId = adID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )

        Spacer(modifier = Modifier.size(24.dp))
    }
}