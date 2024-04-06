package com.chepics.chepics.feature.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.mock.mockComment1
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.getDateTimeString

@Preview
@Composable
fun CommentCell(comment: Comment = mockComment1, onTapImage: (Int) -> Unit = {}) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = comment.user.profileImageUrl,
                    contentDescription = "user icon",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Row {
                        Spacer(
                            modifier = Modifier
                                .width(4.dp)
                                .height(24.dp)
                                .background(
                                    color = ChepicsPrimary,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )

                        Text(
                            text = comment.comment,
                            fontSize = 16.sp,
                            color = ChepicsPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = comment.link.toString(),
                        modifier = Modifier
                            .clickable {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(comment.link)))
                            },
                        color = Color.Blue
                    )

                    comment.images?.let { images ->
                        val imageUrlList = images.map { it.url }
                        GridImagesView(imageUrlList = imageUrlList) {
                            onTapImage(it)
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "like icon")

                        Text(text = "${comment.votes}")
                    }
                }
            }

            HorizontalDivider()
        }
    }
}