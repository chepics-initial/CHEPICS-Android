package com.chepics.chepics.feature.feed

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.common.CommonProgressSpinner
import com.chepics.chepics.feature.common.ImagePager
import com.chepics.chepics.feature.feed.viewparts.TopicCell
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    val topicCoroutineScope = rememberCoroutineScope()
    val commentCoroutineScope = rememberCoroutineScope()

    Box {
        Column(verticalArrangement = Arrangement.Top) {
            TabRow(
                selectedTabIndex = viewModel.selectedTab.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                feedTabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = viewModel.selectedTab.value == index,
                        onClick = {
                            if (viewModel.selectedTab.value == index) {
                                when (viewModel.selectedTab.value) {
                                    0 -> {
                                        topicCoroutineScope.launch {
                                            viewModel.topicScrollState.value.animateScrollToItem(0)
                                        }
                                    }

                                    1 -> {
                                        commentCoroutineScope.launch {
                                        }
                                    }
                                }
                                return@Tab
                            }
                            viewModel.selectTab(index)
                        },
                        text = {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        selectedContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unselectedContentColor = Color.LightGray
                    )
                }
            }
            when (viewModel.selectedTab.value) {
                0 -> {
                    FeedTopicContentView(
                        viewModel = viewModel,
                        showImageViewer = showImageViewer,
                        navController = navController
                    )
                }

                1 -> {
                    Text(text = "Comments")
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.topicImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.topicImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

@Composable
fun FeedTopicContentView(
    viewModel: FeedViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    when (viewModel.topicUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }

        UIState.SUCCESS -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = viewModel.topicScrollState.value
            ) {
                items(viewModel.topics.value) {
                    Card(
                        onClick = {
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RectangleShape
                    ) {
                        TopicCell(topic = it) { index ->
                            it.images?.let { images ->
                                viewModel.onTapImage(index = index, images = images.map { image ->
                                    image.url
                                })
                                showImageViewer.value = true
                            }
                        }
                    }
                }
            }
        }

        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}