package com.chepics.chepics.feature.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.common.CommonProgressSpinner
import com.chepics.chepics.feature.common.ImagePager
import com.chepics.chepics.feature.feed.viewparts.TopicCell

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }
    Box {
        Column(verticalArrangement = Arrangement.Top) {
            Row(modifier = Modifier.fillMaxWidth()) {
                FeedTabType.entries.forEach {
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
            when (viewModel.selectedTab.value) {
                FeedTabType.TOPICS -> {
                    FeedTopicContentView(viewModel = viewModel)
                }
                FeedTabType.COMMENTS -> {
                    Text(text = "Comments")
                }
            }
        }
        
        if (showImageViewer.value) {

        }
    }
}

@Composable
fun FeedTopicContentView(viewModel: FeedViewModel) {
    when (viewModel.topicUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }
        UIState.SUCCESS -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.topics.value) {
                    TopicCell(topic = it)
                }
            }
        }
        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}