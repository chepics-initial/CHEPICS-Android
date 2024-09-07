package com.chepics.chepics.feature.feed

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.comment.CommentDetailNavigationItem
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.BannerAdView
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.FooterView
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.TopicCell
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.feature.topic.top.TopicTopNavigationItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    if (viewModel.showLikeCommentFailureDialog.value) {
        Toast.makeText(
            LocalContext.current,
            "選択していないセットのコメントにはいいねをすることができません",
            Toast.LENGTH_SHORT
        )
            .show()
        viewModel.showLikeCommentFailureDialog.value = false
    }

    if (viewModel.showLikeReplyFailureDialog.value) {
        Toast.makeText(
            LocalContext.current,
            "参加していないトピックの返信にはいいねをすることができません",
            Toast.LENGTH_SHORT
        )
            .show()
        viewModel.showLikeReplyFailureDialog.value = false
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screens.ExploreTopScreen.name) }) {
                            Surface(
                                modifier = Modifier
                                    .clip(CircleShape),
                                color = Color.LightGray
                            ) {
                                Image(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "search icon",
                                    colorFilter = ColorFilter.tint(color = Color.Gray),
                                    modifier = Modifier
                                        .padding(all = 4.dp)
                                )
                            }
                        }
                    }
                )
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
                TabRow(
                    selectedTabIndex = viewModel.selectedTab.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .zIndex(1f)
                ) {
                    feedTabItems.forEachIndexed { index, item ->
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
                when (viewModel.selectedTab.value) {
                    0 -> {
                        FeedTopicContentView(
                            viewModel = viewModel,
                            showImageViewer = showImageViewer,
                            navController = navController
                        )
                    }

                    1 -> {
                        FeedCommentContentView(
                            viewModel = viewModel,
                            showImageViewer = showImageViewer,
                            navController = navController
                        )
                    }
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.feedImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.feedImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopicContentView(
    viewModel: FeedViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    val onRefresh: () -> Unit = {
        viewModel.onTopicRefresh()
    }

    when (viewModel.topicUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }

        UIState.SUCCESS -> {
            PullToRefreshBox(
                isRefreshing = viewModel.isTopicRefreshing.value,
                onRefresh = { onRefresh() }) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = viewModel.topicScrollState.value
                ) {
                    itemsIndexed(viewModel.topics.value) { index, topic ->
                        TopicCell(
                            topic = topic,
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    Screens.TopicTopScreen.name + "/${
                                        TopicTopNavigationItem(
                                            topicId = topic.id,
                                            topic = topic
                                        )
                                    }"
                                )
                            },
                            onTapImage = { imageIndex ->
                                topic.images?.let { images ->
                                    viewModel.onTapImage(
                                        index = imageIndex,
                                        images = images.map { image ->
                                            image.url
                                        })
                                    showImageViewer.value = true
                                }
                            }
                        ) { user ->
                            navController.navigate(Screens.ProfileScreen.name + "/${user}")
                        }

                        if (index % 5 == 4) {
                            FeedBannerAdView()
                        }
                    }

                    item {
                        LaunchedEffect(Unit) {
                            viewModel.onReachTopicFooterView()
                        }

                        FooterView(status = viewModel.topicFooterStatus.value)
                    }
                }
            }
        }

        UIState.FAILURE -> {
            PullToRefreshBox(
                isRefreshing = viewModel.isTopicRefreshing.value,
                onRefresh = { onRefresh() }) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedCommentContentView(
    viewModel: FeedViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    val onRefresh: () -> Unit = {
        viewModel.onCommentRefresh()
    }

    when (viewModel.commentUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }

        UIState.SUCCESS -> {
            PullToRefreshBox(
                isRefreshing = viewModel.isCommentRefreshing.value,
                onRefresh = { onRefresh() }) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = viewModel.commentScrollState.value
                ) {
                    viewModel.comments.value?.let { comments ->
                        itemsIndexed(comments) { index, comment ->
                            CommentCell(
                                comment = comment,
                                type = CommentType.COMMENT,
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        Screens.CommentDetailScreen.name + "/${
                                            CommentDetailNavigationItem(
                                                commentId = comment.id,
                                                comment = comment,
                                                isTopicTitleEnabled = true
                                            )
                                        }"
                                    )
                                },
                                onTapImage = { imageIndex ->
                                    comment.images?.let { images ->
                                        viewModel.onTapImage(
                                            index = imageIndex,
                                            images = images.map { image ->
                                                image.url
                                            })
                                        showImageViewer.value = true
                                    }
                                },
                                onTapUserInfo = { user ->
                                    navController.navigate(Screens.ProfileScreen.name + "/${user}")
                                },
                                onTapLikeButton = {
                                    viewModel.onTapLikeButton(comment)
                                },
                                onTapTopicTitle = {
                                    navController.navigate(
                                        Screens.TopicTopScreen.name + "/${
                                            TopicTopNavigationItem(
                                                topicId = comment.topicId,
                                                topic = null
                                            )
                                        }"
                                    )
                                }
                            )

                            if (index % 5 == 4) {
                                FeedBannerAdView()
                            }
                        }

                        item {
                            LaunchedEffect(Unit) {
                                viewModel.onReachCommentFooterView()
                            }

                            FooterView(status = viewModel.commentFooterStatus.value)
                        }
                    }
                }
            }
        }

        UIState.FAILURE -> {
            PullToRefreshBox(
                isRefreshing = viewModel.isCommentRefreshing.value,
                onRefresh = { onRefresh() }) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedBannerAdView() {
    BannerAdView(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        adID = "ca-app-pub-3940256099942544/9214589741"
    )
}