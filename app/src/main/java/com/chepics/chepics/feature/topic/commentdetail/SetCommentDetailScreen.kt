package com.chepics.chepics.feature.topic.commentdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import com.chepics.chepics.feature.createcomment.CreateCommentNavigationItem
import com.chepics.chepics.feature.createcomment.CreateCommentType
import com.chepics.chepics.feature.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetCommentDetailScreen(
    set: PickSet,
    comment: Comment,
    navController: NavController,
    viewModel: SetCommentDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onStart(set = set, comment = comment)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Logo Icon",
                        modifier = Modifier.clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onBack()
                        navController.navigateUp()
                    }
                    ) {
                        Image(
                            imageVector = Icons.Default.Close,
                            contentDescription = "close icon"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            viewModel.set.value?.let { selectedSet ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = selectedSet.name,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "20%",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.black_people),
                                contentDescription = "set count icon",
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "${selectedSet.votes}",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            LazyColumn {
                viewModel.comment.value?.let { comment ->
                    item {
                        CommentCell(
                            comment = comment,
                            type = CommentType.DETAIL,
                            onTapImage = {},
                            onTapUserInfo = {},
                            onTapReplyButton = {
                                navController.navigate(
                                    Screens.CreateCommentScreen.name + "/${
                                        CreateCommentNavigationItem(
                                            topicId = comment.topicId,
                                            setId = comment.setId,
                                            type = CreateCommentType.REPLY
                                        )
                                    }"
                                )
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chat),
                                contentDescription = "chat icon",
                                colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "リプライ2件",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                items(viewModel.replies.value) { reply ->
                    CommentCell(
                        comment = reply,
                        type = CommentType.REPLY,
                        onTapImage = {},
                        onTapUserInfo = {},
                        onTapReplyButton = {
                            navController.navigate(
                                Screens.CreateCommentScreen.name + "/${
                                    CreateCommentNavigationItem(
                                        topicId = comment.topicId,
                                        setId = comment.setId,
                                        type = CreateCommentType.REPLY,
                                        replyFor = reply
                                    )
                                }"
                            )
                        }
                    )
                }
            }
        }
    }
}