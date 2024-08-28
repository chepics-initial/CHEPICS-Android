package com.chepics.chepics.feature.topic.top.viewparts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.feature.comment.CommentDetailNavigationItem
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.FooterView
import com.chepics.chepics.feature.commonparts.IconScale
import com.chepics.chepics.feature.commonparts.UserIcon
import com.chepics.chepics.feature.createcomment.CreateCommentNavigationItem
import com.chepics.chepics.feature.createcomment.CreateCommentType
import com.chepics.chepics.feature.navigation.NavigationParts
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.feature.topic.top.TopicTopViewModel
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.getDateTimeString

@Composable
fun TopicTopDetailView(
    navController: NavController,
    viewModel: TopicTopViewModel,
    onTapImage: (Int, List<String>) -> Unit
) {
    val createCommentCompletion: () -> Unit = {
        viewModel.createCommentCompletion()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        viewModel.topic.value?.let { topic ->
            viewModel.selectedSet.value?.let { set ->
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        DetailHeaderView(
                            navController = navController,
                            topic = topic
                        ) { index, images ->
                            onTapImage(index, images)
                        }

                        DetailSetView(set = set) {
                            viewModel.showBottomSheet.value = true
                        }

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chat),
                                contentDescription = "chat icon",
                                colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "コメント${set.commentCount}件",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    when (viewModel.commentUIState.value) {
                        UIState.LOADING -> {
                            item {
                                CommonProgressSpinner(Color.Transparent)
                            }
                        }

                        UIState.SUCCESS -> {
                            viewModel.comments.value?.let { comments ->
                                items(comments) { comment ->
                                    CommentCell(
                                        comment = comment,
                                        type = CommentType.SET,
                                        modifier = Modifier.clickable {
                                            navController.navigate(
                                                Screens.CommentDetailScreen.name + "/${
                                                    CommentDetailNavigationItem(
                                                        commentId = comment.id,
                                                        comment = comment,
                                                        isTopicTitleEnabled = false
                                                    )
                                                }"
                                            )
                                        },
                                        onTapImage = { index ->
                                            comment.images?.let { images ->
                                                onTapImage(index, images.map { it.url })
                                            }
                                        },
                                        onTapUserInfo = { user ->
                                            navController.navigate(Screens.ProfileScreen.name + "/${user}")
                                        },
                                        onTapLikeButton = {
                                            viewModel.onTapLikeButton(comment)
                                        }, onTapTopicTitle = {}
                                    )
                                }

                                item {
                                    LaunchedEffect(Unit) {
                                        viewModel.onReachCommentFooterView()
                                    }

                                    FooterView(status = viewModel.commentFooterStatus.value)
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

        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider()

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.clickable {
                        viewModel.topic.value?.let { topic ->
                            viewModel.selectedSet.value?.let { selectedSet ->
                                navController.navigate(
                                    Screens.CreateCommentScreen.name + "/${
                                        CreateCommentNavigationItem(
                                            topicId = topic.id,
                                            setId = selectedSet.id,
                                            parentId = null,
                                            type = CreateCommentType.COMMENT
                                        )
                                    }"
                                ) {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        NavigationParts.createCommentCompletion,
                                        createCommentCompletion
                                    )
                                }
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "chat icon",
                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = "コメントする")
                }
            }
        }
    }
}

@Composable
fun DetailHeaderView(
    navController: NavController,
    topic: Topic,
    onTapImage: (Int, List<String>) -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "topic",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = ChepicsPrimary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, ChepicsPrimary)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.persons),
                            contentDescription = "chart",
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        val votesText = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(topic.votes.toString())
                            }

                            append("人が参加中")
                        }

                        Text(
                            text = votesText,
                            style = MaterialTheme.typography.labelMedium,
                            color = ChepicsPrimary
                        )
                    }

                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    topic.link?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(it)
                                        )
                                    )
                                },
                            color = Color.Blue
                        )
                    }
                }

                topic.images?.let { images ->
                    val imageUrlList = images.map { it.url }
                    if (imageUrlList.size > 1) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .height(if (imageUrlList.size == 4) (getTopicDetailImageHeight() * 2 + 28.dp) else getTopicDetailImageHeight() + 20.dp),
                            contentPadding = PaddingValues(
                                top = 12.dp,
                                start = 12.dp,
                                end = 12.dp
                            )
                        ) {
                            items(imageUrlList.size) { index ->
                                if (!(imageUrlList.size % 2 != 0 && index == imageUrlList.size - 1)) {
                                    AsyncImage(
                                        model = imageUrlList[index],
                                        contentDescription = "$index image",
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Gray)
                                            .clickable { onTapImage(index, imageUrlList) },
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }

                    if (imageUrlList.size % 2 == 1) {
                        if (imageUrlList.size == 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        AsyncImage(
                            model = imageUrlList.last(),
                            contentDescription = "last image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(getTopicDetailImageHeight())
                                .padding(
                                    top = 4.dp,
                                    bottom = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                                .clickable {
                                    onTapImage(
                                        imageUrlList.size - 1,
                                        imageUrlList
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.navigate(Screens.ProfileScreen.name + "/${topic.user}") }
                    ) {
                        UserIcon(url = topic.user.profileImageUrl, scale = IconScale.TOPIC)

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = topic.user.fullname,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = getDateTimeString(topic.registerTime),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { navController.navigate(Screens.TopicDetailScreen.name + "/${topic}") }
                ) {
                    Text(
                        text = "トピックの詳細を見る",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelSmall
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "detail icon",
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.LightGray)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailSetView(
    set: PickSet,
    onTapShowSetList: () -> Unit
) {
    Column {
        Text(
            text = "set",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.Blue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "check icon",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "参加中",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.black_people),
                        contentDescription = "people icon",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = set.votes.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }

                Text(
                    text = set.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Blue),
                modifier = Modifier.clickable { onTapShowSetList() }
            ) {
                Text(
                    text = "全てのセットを見る",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Blue,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun getTopicDetailImageHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 74) / 2).dp
}