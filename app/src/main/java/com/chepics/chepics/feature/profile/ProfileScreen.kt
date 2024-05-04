package com.chepics.chepics.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.TopicCell
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.mock.mockTopicImage1
import com.chepics.chepics.ui.theme.ChepicsPrimary
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    val topicCoroutineScope = rememberCoroutineScope()
    val commentCoroutineScope = rememberCoroutineScope()

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
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

            Scaffold(
                topBar = {
                    TabRow(
                        selectedTabIndex = viewModel.selectedTab.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        profileTabItems.forEachIndexed { index, item ->
                            Tab(
                                selected = viewModel.selectedTab.value == index,
                                onClick = {
                                    if (viewModel.selectedTab.value == index) {
                                        when (viewModel.selectedTab.value) {
                                            0 -> {
                                                topicCoroutineScope.launch {
                                                    viewModel.topicScrollState.value.animateScrollToItem(
                                                        0
                                                    )
                                                }
                                            }

                                            1 -> {
                                                commentCoroutineScope.launch {
                                                    viewModel.commentScrollState.value.animateScrollToItem(
                                                        0
                                                    )
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
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screens.CreateTopicScreen.name) },
                        shape = CircleShape,
                        containerColor = Color.Blue
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add a Topic",
                            tint = Color.White
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    verticalArrangement = Arrangement.Top
                ) {
                    when (viewModel.selectedTab.value) {
                        0 -> {
                            ProfileTopicContentView(
                                viewModel = viewModel,
                                showImageViewer = showImageViewer,
                                navController = navController
                            )
                        }

                        1 -> {
                            ProfileCommentContentView(
                                viewModel = viewModel,
                                showImageViewer = showImageViewer,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.profileImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.profileImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

@Composable
fun ProfileTopicContentView(
    viewModel: ProfileViewModel,
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

        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}

@Composable
fun ProfileCommentContentView(
    viewModel: ProfileViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    when (viewModel.commentUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }
        UIState.SUCCESS -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = viewModel.commentScrollState.value
            ) {
                items(viewModel.comments.value) {
                    CommentCell(comment = it) { index ->
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
        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}