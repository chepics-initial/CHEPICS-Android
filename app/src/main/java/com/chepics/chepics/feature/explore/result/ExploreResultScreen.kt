package com.chepics.chepics.feature.explore.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
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
import com.chepics.chepics.feature.explore.viewparts.AutoCompleteView
import com.chepics.chepics.feature.explore.viewparts.ExploreTopBar
import com.chepics.chepics.feature.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val isFocused = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    val isNavigationEnabled = remember {
        mutableStateOf(true)
    }

    if (!isNavigationEnabled.value) {
        LaunchedEffect(key1 = true) {
            delay(1000L)
            isNavigationEnabled.value = true
        }
    }

    val focusRequester = remember {
        FocusRequester()
    }

    BackHandler {
        if (isFocused.value) {
            isFocused.value = false
            viewModel.searchText.value = viewModel.initialSearchText
            focusManager.clearFocus()
        } else {
            navController.popBackStack()
        }
    }

    Box {
        Scaffold(
            topBar = {
                ExploreTopBar(
                    focusRequester = focusRequester,
                    searchText = viewModel.searchText,
                    showTrailingIcon = viewModel.searchText.value.isNotEmpty() && isFocused.value,
                    modifier = Modifier.onFocusChanged {
                        isFocused.value = it.isFocused
                    },
                    onTapBackButton = {
                        if (isFocused.value) {
                            isFocused.value = false
                            viewModel.searchText.value = viewModel.initialSearchText
                            focusManager.clearFocus()
                        } else {
                            navController.popBackStack()
                        }
                    }
                ) {
                    navController.navigate(Screens.ExploreResultScreen.name + "/${viewModel.searchText.value}")
                }
            }
        ) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                if (!isFocused.value) {
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
                                isNavigationEnabled = isNavigationEnabled,
                                navController = navController
                            )
                        }
                    }
                } else {
                    AutoCompleteView(searchText = viewModel.searchText) {
                        navController.navigate(Screens.ExploreResultScreen.name + "/${viewModel.searchText.value}")
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
    isNavigationEnabled: MutableState<Boolean>,
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
                        UserCell(
                            user = it,
                            modifier = Modifier.clickable {
                                if (isNavigationEnabled.value) {
                                    isNavigationEnabled.value = false
                                    navController.navigate(Screens.ProfileScreen.name + "/hello")
                                }
                            }
                        )
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