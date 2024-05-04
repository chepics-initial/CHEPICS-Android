package com.chepics.chepics.feature.commonparts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun UserIcon(url: String?, scale: IconScale) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = "user icon",
            modifier = Modifier
                .size(scale.scaleValue())
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Surface(
            modifier = Modifier
                .size(scale.scaleValue())
                .clip(CircleShape),
            color = ChepicsPrimary.copy(0.4f)
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "user icon",
                colorFilter = ColorFilter.tint(color = ChepicsPrimary),
                modifier = Modifier
                    .padding(all = 4.dp)
            )
        }
    }
}

enum class IconScale {
    TOPIC,
    COMMENT,
    USER,
    PROFILE;

    fun scaleValue(): Dp {
        when (this) {
            TOPIC -> return 24.dp
            COMMENT -> return 32.dp
            USER -> return 40.dp
            PROFILE -> return 64.dp
        }
    }
}