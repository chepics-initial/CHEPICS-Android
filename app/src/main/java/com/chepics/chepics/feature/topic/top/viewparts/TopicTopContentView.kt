package com.chepics.chepics.feature.topic.top.viewparts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.feature.commonparts.IconScale
import com.chepics.chepics.feature.commonparts.UserIcon
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.feature.topic.top.TopicTopViewModel
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.getDateTimeString

@Composable
fun TopicTopContentView(
    viewModel: TopicTopViewModel,
    navController: NavController,
    onTapShowSetList: () -> Unit,
    onTapImage: (Int, List<String>) -> Unit
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Text(
            text = "topic",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = ChepicsPrimary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        viewModel.topic.value?.let { topic ->
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                topic.description?.let {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                topic.link?.let {
                    Spacer(modifier = Modifier.height(16.dp))

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
                            .height(if (imageUrlList.size == 4) (getTopicTopImageHeight() * 2 + 28.dp) else getTopicTopImageHeight() + 20.dp),
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
                            .height(getTopicTopImageHeight())
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.persons),
                        contentDescription = "chart",
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = topic.votes.toString(),
                        color = ChepicsPrimary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

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
                }

                Text(
                    text = getDateTimeString(topic.registerTime),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }

            HorizontalDivider()

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "set",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Blue
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "セットを選択してください",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Blue),
                        modifier = Modifier.clickable { onTapShowSetList() }
                    ) {
                        Text(
                            text = "セット一覧を見る",
                            color = Color.Blue,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getTopicTopImageHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 40) / 2).dp
}