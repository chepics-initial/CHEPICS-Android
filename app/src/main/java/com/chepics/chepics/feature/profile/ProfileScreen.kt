package com.chepics.chepics.feature.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.IconScale
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.TopicCell
import com.chepics.chepics.feature.commonparts.UserIcon
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: User,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    val topicCoroutineScope = rememberCoroutineScope()
    val commentCoroutineScope = rememberCoroutineScope()

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onStart(user)
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Logo Icon",
                        modifier = Modifier.clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                })
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                viewModel.user.value?.let {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            UserIcon(url = it.profileImageUrl, scale = IconScale.PROFILE)

                            if (viewModel.isCurrentUser.value) {
                                IconButton(onClick = {
                                    viewModel.user.value?.let {
                                        navController.navigate(Screens.EditProfileScreen.name + "/${it}")
                                    }
                                }) {
                                    Image(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "edit profile",
                                        colorFilter = ColorFilter.tint(color = ChepicsPrimary)
                                    )
                                }
                            } else if (viewModel.isFollowing.value != null) {
                                viewModel.isFollowing.value?.let { isFollowing ->
                                    Surface(
                                        modifier = Modifier.clickable {
                                            if (viewModel.isEnabled.value) {
                                                viewModel.onTapFollowButton()
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        color = ChepicsPrimary
                                    ) {
                                        Text(
                                            text = if (isFollowing) "フォロー中" else "フォローする",
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = it.fullname,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "@${it.username}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        it.bio?.let { bio ->
                            Text(
                                text = bio,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        it.followers?.let { followers ->
                            it.following?.let { following ->
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$following",
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
                                        text = "$followers",
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
                        }
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
        }

        if (viewModel.showDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "通信エラー") },
                text = { Text(text = "インターネット環境を確認して、もう一度お試しください。") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
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
                    TopicCell(
                        topic = it,
                        modifier = Modifier.clickable { navController.navigate(Screens.TopicTopScreen.name + "/${it}") },
                        onTapImage = { index ->
                            it.images?.let { images ->
                                viewModel.onTapImage(index = index, images = images.map { image ->
                                    image.url
                                })
                                showImageViewer.value = true
                            }
                        }
                    ) { user ->
                        navController.navigate(Screens.ProfileScreen.name + "/${user}")
                    }
                }
            }
        }

        UIState.FAILURE -> {
            Text(
                text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                modifier = Modifier.padding(16.dp)
            )
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
                    CommentCell(
                        comment = it,
                        type = CommentType.COMMENT,
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.CommentDetailScreen.name + "/${it}")
                        },
                        onTapImage = { index ->
                            it.images?.let { images ->
                                viewModel.onTapImage(index = index, images = images.map { image ->
                                    image.url
                                })
                                showImageViewer.value = true
                            }
                        },
                        onTapUserInfo = { user ->
                            navController.navigate(Screens.ProfileScreen.name + "/${user}")
                        }
                    )
                }
            }
        }

        UIState.FAILURE -> {
            Text(
                text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}