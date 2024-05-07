package com.chepics.chepics.feature.explore.viewparts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun AutoCompleteView(
    searchText: MutableState<String>,
    onTapCell: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onTapCell()
                }
                .padding(16.dp)
        ) {
            Image(
                imageVector = Icons.Default.Search,
                contentDescription = "search icon",
                colorFilter = ColorFilter.tint(color = ChepicsPrimary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = searchText.value,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "を検索")
        }

        HorizontalDivider()
    }
}