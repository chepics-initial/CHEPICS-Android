package com.chepics.chepics.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.feature.feed.FeedTabType
import com.chepics.chepics.feature.feed.FeedViewModel
import com.chepics.chepics.mock.mockTopicImage1
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = mockTopicImage1.url,
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier.clickable {  },
                    shape = RoundedCornerShape(8.dp),
                    color = ChepicsPrimary
                ) {
                    Text(
                        text = "フォローする",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "太郎",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "@aabbcc",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "動物に関するトピックを投稿しています\nよろしく",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "20",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "フォロー",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "20",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "フォロワー",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            ProfileTabType.entries.forEach {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            viewModel.selectTab(it)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it.getTitle(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp),
                        color = if (viewModel.selectedTab.value == it) (if (isSystemInDarkTheme()) Color.White else Color.Black) else Color.LightGray
                    )

                    HorizontalDivider(color = if (viewModel.selectedTab.value == it) (if (isSystemInDarkTheme()) Color.White else Color.Black) else Color.LightGray)
                }
            }
        }
    }
}