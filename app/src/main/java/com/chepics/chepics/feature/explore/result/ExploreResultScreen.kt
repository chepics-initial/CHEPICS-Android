package com.chepics.chepics.feature.explore.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.TopicCell
import com.chepics.chepics.feature.commonparts.UserCell
import com.chepics.chepics.ui.theme.ChepicsPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreResultScreen(
    navController: NavController,
    searchText: String,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: ExploreResultViewModel = hiltViewModel()
) {
    val topicCoroutineScope = rememberCoroutineScope()
    val commentCoroutineScope = rememberCoroutineScope()
    val userCoroutineScope = rememberCoroutineScope()
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onAppear(searchText)
    }
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = "Logo Icon",
                            modifier = Modifier.clickable { navController.popBackStack() }
                        )

                        Surface(
                            shape = CircleShape,
                            color = Color.LightGray.copy(0.8f),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            TextField(
                                value = viewModel.searchText.value,
                                onValueChange = { viewModel.searchText.value = it },
                                textStyle = TextStyle(fontSize = 16.sp),
                                label = { Text(text = "検索") },
                                leadingIcon = {
                                    Image(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "search icon",
                                        colorFilter = ColorFilter.tint(color = Color.Gray)
                                    )
                                },
                                trailingIcon = {
                                    if (viewModel.searchText.value.isNotEmpty()) {
                                        Image(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "search icon",
                                            colorFilter = ColorFilter.tint(color = Color.Gray),
                                            modifier = Modifier
                                                .clickable { viewModel.searchText.value = "" }
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = ChepicsPrimary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                )
                            )
                        }
                    }
                })
            }
        ) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                TabRow(
                    selectedTabIndex = viewModel.selectedTab.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .zIndex(1f)
                ) {
                    searchTabItems.forEachIndexed { index, item ->
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

                                        2 -> {
                                            userCoroutineScope.launch {
                                                viewModel.userScrollState.value.animateScrollToItem(
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
                        ExploreTopicContentView(
                            viewModel = viewModel,
                            showImageViewer = showImageViewer,
                            navController = navController
                        )
                    }

                    1 -> {
                        ExploreCommentContentView(
                            viewModel = viewModel,
                            showImageViewer = showImageViewer,
                            navController = navController
                        )
                    }

                    2 -> {
                        ExploreUserContentView(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.searchImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.searchImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopicContentView(
    viewModel: ExploreResultViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.fetchTopics()
            refreshState.endRefresh()
        }
    }

    when (viewModel.topicUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }

        UIState.SUCCESS -> {
            Box(Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
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

                PullToRefreshContainer(
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

        UIState.FAILURE -> {
            Text(text = "通信に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreCommentContentView(
    viewModel: ExploreResultViewModel,
    showImageViewer: MutableState<Boolean>,
    navController: NavController
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.fetchComments()
            refreshState.endRefresh()
        }
    }

    when (viewModel.commentUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }
        UIState.SUCCESS -> {
            Box(Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
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

                PullToRefreshContainer(
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreUserContentView(
    viewModel: ExploreResultViewModel,
    navController: NavController
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.fetchUsers()
            refreshState.endRefresh()
        }
    }

    when (viewModel.userUIState.value) {
        UIState.LOADING -> {
            CommonProgressSpinner(backgroundColor = Color.Transparent)
        }
        UIState.SUCCESS -> {
            Box(Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = viewModel.userScrollState.value
                ) {
                    items(viewModel.users.value) {
                        UserCell(user = it)
                    }
                }

                PullToRefreshContainer(
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
        UIState.FAILURE -> {
            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
        }
    }
}