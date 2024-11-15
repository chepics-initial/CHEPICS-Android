package com.chepics.chepics.feature.comment

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import androidx.navigation.NavController
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.FooterView
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.createcomment.CreateCommentNavigationItem
import com.chepics.chepics.feature.createcomment.CreateCommentType
import com.chepics.chepics.feature.navigation.NavigationParts
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.feature.topic.top.TopicTopNavigationItem
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDetailScreen(
    navigationItem: CommentDetailNavigationItem,
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: CommentDetailViewModel = hiltViewModel()
) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    val replyFor: MutableState<Comment?> = remember {
        mutableStateOf(null)
    }

    val createReplyCompletion: () -> Unit = {
        viewModel.createReplyCompletion()
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onStart(navigationItem.commentId, navigationItem.comment)
    }

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

    if (viewModel.showReplyRestrictionDialog.value) {
        Toast.makeText(
            LocalContext.current,
            "トピック内でセットを選択することでリプライが可能になります",
            Toast.LENGTH_SHORT
        )
            .show()
        viewModel.showReplyRestrictionDialog.value = false
    }

    if (viewModel.showCreateCommentScreen.value) {
        viewModel.rootComment.value?.let { repliedComment ->
            navController.navigate(
                Screens.CreateCommentScreen.name + "/${
                    CreateCommentNavigationItem(
                        topicId = repliedComment.topicId,
                        setId = repliedComment.setId,
                        parentId = repliedComment.id,
                        type = CreateCommentType.REPLY,
                        replyFor = replyFor.value
                    )
                }"
            ) {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    NavigationParts.createCommentCompletion,
                    createReplyCompletion
                )
            }
        }
        viewModel.showCreateCommentScreen.value = false
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
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (viewModel.headerUIState.value) {
                        UIState.LOADING -> {
                            item {
                                CommonProgressSpinner(backgroundColor = Color.Transparent)
                            }
                        }

                        UIState.SUCCESS -> {
                            item {
                                viewModel.rootComment.value?.let { rootComment ->
                                    CommentCell(
                                        comment = rootComment,
                                        type = if (navigationItem.isTopicTitleEnabled) CommentType.DETAIL else CommentType.TOPICCOMMENTDETAIL,
                                        onTapImage = { index ->
                                            rootComment.images?.let { images ->
                                                viewModel.onTapImage(
                                                    index = index,
                                                    images = images.map { image ->
                                                        image.url
                                                    })
                                                showImageViewer.value = true
                                            }
                                        }, onTapUserInfo = { user ->
                                            navController.navigate(Screens.ProfileScreen.name + "/${user}")
                                        }, onTapLikeButton = {
                                            viewModel.onTapLikeButton(rootComment)
                                        }, onTapReplyButton = {
                                            replyFor.value = null
                                            viewModel.onTapReplyButton(rootComment)
                                        }, onTapTopicTitle = {
                                            if (navigationItem.isTopicTitleEnabled) {
                                                navController.navigate(
                                                    Screens.TopicTopScreen.name + "/${
                                                        TopicTopNavigationItem(
                                                            topicId = rootComment.topicId,
                                                            topic = null
                                                        )
                                                    }"
                                                )
                                            }
                                        }
                                    )

                                    viewModel.rootComment.value?.parentId?.let { parentId ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Row(modifier = Modifier.clickable {
                                                navController.navigate(
                                                    Screens.CommentDetailScreen.name + "/${
                                                        CommentDetailNavigationItem(
                                                            commentId = parentId,
                                                            comment = null,
                                                            isTopicTitleEnabled = navigationItem.isTopicTitleEnabled
                                                        )
                                                    }"
                                                )
                                            }) {
                                                Text(
                                                    text = "リプライ元のコメントを見る",
                                                    color = ChepicsPrimary
                                                )

                                                Spacer(modifier = Modifier.width(16.dp))

                                                Image(
                                                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                                    contentDescription = "arrow icon",
                                                    colorFilter = ColorFilter.tint(color = ChepicsPrimary)
                                                )
                                            }
                                        }
                                    } ?: run {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Reply",
                                                fontWeight = FontWeight.SemiBold
                                            )

                                            Spacer(modifier = Modifier.width(16.dp))

                                            rootComment.replyCount?.let { replyCount ->
                                                Text(
                                                    text = "$replyCount 件の返信",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = Color.LightGray
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (viewModel.rootComment.value?.parentId == null) {
                                when (viewModel.uiState.value) {
                                    UIState.LOADING -> {
                                        item {
                                            CommonProgressSpinner(backgroundColor = Color.Transparent)
                                        }
                                    }

                                    UIState.SUCCESS -> {
                                        viewModel.replies.value?.let { replies ->
                                            items(replies) { reply ->
                                                CommentCell(
                                                    comment = reply,
                                                    type = CommentType.REPLY,
                                                    onTapImage = { index ->
                                                        reply.images?.let { images ->
                                                            viewModel.onTapImage(
                                                                index = index,
                                                                images = images.map { image ->
                                                                    image.url
                                                                })
                                                            showImageViewer.value = true
                                                        }
                                                    }, onTapUserInfo = { user ->
                                                        navController.navigate(Screens.ProfileScreen.name + "/${user}")
                                                    },
                                                    onTapLikeButton = {
                                                        viewModel.onTapLikeButton(reply)
                                                    }, onTapReplyButton = {
                                                        replyFor.value = reply
                                                        viewModel.onTapReplyButton(reply)
                                                    }, onTapTopicTitle = {}
                                                )
                                            }

                                            item {
                                                LaunchedEffect(Unit) {
                                                    viewModel.onReachFooterView()
                                                }

                                                FooterView(status = viewModel.footerStatus.value)
                                            }
                                        }
                                    }

                                    UIState.FAILURE -> {
                                        item {
                                            Text(
                                                text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        UIState.FAILURE -> {
                            item {
                                Text(
                                    text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.commentImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.commentImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

data class CommentDetailNavigationItem(
    val commentId: String,
    val comment: Comment?,
    val isTopicTitleEnabled: Boolean
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class CommentDetailNavigationItemNavType : JsonNavType<CommentDetailNavigationItem>() {
    override fun fromJsonParse(value: String): CommentDetailNavigationItem {
        return Gson().fromJson(value, CommentDetailNavigationItem::class.java)
    }

    override fun CommentDetailNavigationItem.getJsonParse(): String {
        return Gson().toJson(this)
    }
}