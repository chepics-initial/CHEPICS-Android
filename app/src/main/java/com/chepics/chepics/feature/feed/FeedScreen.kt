package com.chepics.chepics.feature.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    Column(verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "おすすめ",
                    modifier = Modifier.padding(8.dp)
                )

                Divider()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "フォロー中",
                    modifier = Modifier.padding(8.dp)
                )

                Divider()
            }
        }
    }
}