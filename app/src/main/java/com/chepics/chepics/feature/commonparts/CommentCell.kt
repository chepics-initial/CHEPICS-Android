package com.chepics.chepics.feature.commonparts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.getDateTimeString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommentCell(
    comment: Comment,
    type: CommentType,
    modifier: Modifier = Modifier,
    onTapImage: (Int) -> Unit,
    onTapUserInfo: (User) -> Unit,
    onTapLikeButton: () -> Unit,
    onTapReplyButton: () -> Unit = {},
    onTapTopicTitle: () -> Unit
) {
    val context = LocalContext.current
    var isLikeButtonEnabled = true
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                UserIcon(
                    url = comment.user.profileImageUrl,
                    scale = IconScale.COMMENT,
                    modifier = Modifier.clickable { onTapUserInfo(comment.user) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onTapUserInfo(comment.user) }
                        ) {
                            Text(
                                text = comment.user.fullname,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "@${comment.user.username}",
                                color = Color.LightGray
                            )
                        }

                        Text(
                            text = getDateTimeString(comment.registerTime),
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    when (type) {
                        CommentType.COMMENT, CommentType.DETAIL -> {
                            Row(modifier = Modifier.clickable { onTapTopicTitle() }) {
                                Spacer(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(24.dp)
                                        .background(
                                            color = ChepicsPrimary,
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = comment.topic,
                                    fontSize = 16.sp,
                                    color = ChepicsPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        CommentType.REPLY, CommentType.SET, CommentType.SETDETAIL, CommentType.SETREPLY, CommentType.TOPICCOMMENTDETAIL -> {
                            Box(modifier = Modifier)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    comment.replyFor?.let { replies ->
                        if (replies.isNotEmpty() && type == CommentType.REPLY) {
                            val user = replies.first()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(
                                        id = R.drawable.reply_cell
                                    ),
                                    contentDescription = "reply icon",
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = user.fullname,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ChepicsPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = comment.comment)

                    Spacer(modifier = Modifier.height(8.dp))

                    comment.link?.let { link ->
                        Text(
                            text = link,
                            modifier = Modifier
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(comment.link)
                                        )
                                    )
                                },
                            color = Color.Blue
                        )
                    }
                }
            }

            comment.images?.let { images ->
                val imageUrlList = images.map { it.url }
                if (imageUrlList.size > 1) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .height(if (imageUrlList.size == 4) (getHeight() * 2 + 16.dp) else getHeight() + 8.dp),
                        contentPadding = PaddingValues(start = 52.dp, end = 12.dp)
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
                                        .clickable { onTapImage(index) },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                if (imageUrlList.size % 2 == 1) {
                    AsyncImage(
                        model = imageUrlList.last(),
                        contentDescription = "last image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(getHeight())
                            .padding(top = 4.dp, bottom = 4.dp, start = 56.dp, end = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                            .clickable { onTapImage(imageUrlList.size - 1) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp, start = 56.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                comment.replyCount?.let {
                    when (type) {
                        CommentType.COMMENT, CommentType.REPLY, CommentType.SET -> {
                            if (it == 1) {
                                Text(
                                    text = "1 reply",
                                    color = ChepicsPrimary
                                )
                            } else if (it > 1) {
                                Text(
                                    text = "$it replies",
                                    color = ChepicsPrimary
                                )
                            }
                        }

                        CommentType.DETAIL, CommentType.SETDETAIL, CommentType.SETREPLY, CommentType.TOPICCOMMENTDETAIL -> {
                            Box(modifier = Modifier)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (type) {
                        CommentType.COMMENT, CommentType.SET, CommentType.SETDETAIL, CommentType.SETREPLY -> {
                            Box(modifier = Modifier)
                        }

                        CommentType.REPLY, CommentType.TOPICCOMMENTDETAIL -> {
                            Image(
                                painter = painterResource(id = R.drawable.reply),
                                contentDescription = "reply icon",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        onTapReplyButton()
                                    }
                            )
                        }

                        CommentType.DETAIL -> {
                            if (comment.parentId == null) {
                                Image(
                                    painter = painterResource(id = R.drawable.reply),
                                    contentDescription = "reply icon",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            onTapReplyButton()
                                        }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        imageVector = if (comment.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "like icon",
                        colorFilter = ColorFilter.tint(color = if (comment.isLiked) Color.Red else if (isSystemInDarkTheme()) Color.White else Color.Black),
                        modifier = Modifier.clickable {
                            if (isLikeButtonEnabled) {
                                isLikeButtonEnabled = false
                                coroutineScope.launch {
                                    onTapLikeButton()
                                    delay(2000L)
                                    isLikeButtonEnabled = true
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "${comment.votes}")
                }
            }

            HorizontalDivider()
        }
    }
}

@Composable
private fun getHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 80) / 2).dp
}

enum class CommentType {
    COMMENT,
    DETAIL,
    REPLY,
    SET,
    SETDETAIL,
    SETREPLY,
    TOPICCOMMENTDETAIL
}