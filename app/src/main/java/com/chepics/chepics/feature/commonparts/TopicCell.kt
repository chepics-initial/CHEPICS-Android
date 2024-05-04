package com.chepics.chepics.feature.commonparts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.getDateTimeString

@Composable
fun TopicCell(topic: Topic, onTapImage: (Int) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = topic.title,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = topic.link.toString(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(topic.link)))
                    },
                color = Color.Blue
            )

            topic.images?.let { images ->
                val imageUrlList = images.map { it.url }
                if (imageUrlList.size > 1) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .height(if (imageUrlList.size == 4) (getHeight() * 2 + 28.dp) else getHeight() + 20.dp),
                        contentPadding = PaddingValues(top = 12.dp, start = 12.dp, end = 12.dp)
                    ) {
                        items(imageUrlList.size) {index ->
                            if (!(imageUrlList.size % 2 != 0 && index == imageUrlList.size - 1)) {
                                AsyncImage(
                                    model = imageUrlList[index],
                                    contentDescription = "$index image",
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onTapImage(index) },
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
                            .height(getHeight())
                            .padding(top = 4.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onTapImage(imageUrlList.size - 1) },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.chart),
                        contentDescription = "chart",
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = topic.votes.toString(),
                        color = ChepicsPrimary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

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

            HorizontalDivider()
        }
    }
}

@Composable
private fun getHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 40) / 2).dp
}