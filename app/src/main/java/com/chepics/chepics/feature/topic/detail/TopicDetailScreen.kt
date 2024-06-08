package com.chepics.chepics.feature.topic.detail

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.feature.commonparts.IconScale
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.UserIcon
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topic: Topic,
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>
) {
    val context = LocalContext.current
    val showImageViewer = remember {
        mutableStateOf(false)
    }
    val selectedImageIndex: MutableState<Int?> = remember {
        mutableStateOf(null)
    }

    val images: MutableState<List<String>?> = remember {
        mutableStateOf(topic.images?.map { it.url })
    }

    val scrollState = rememberScrollState()

    fun onTapImage(index: Int) {
        selectedImageIndex.value = index
        showImageViewer.value = true
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Logo Icon",
                        modifier = Modifier.clickable { navController.popBackStack() },
                        colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                })
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = topic.title,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.SemiBold
                )

                topic.description?.let { description ->
                    Text(
                        text = description,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                topic.link?.let { link ->
                    Text(
                        text = link,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(topic.link)
                                    )
                                )
                            },
                        color = Color.Blue
                    )
                }

                topic.images?.let { images ->
                    val imageUrlList = images.map { it.url }
                    if (imageUrlList.size > 1) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(if (imageUrlList.size == 4) (getTopicDetailHeight() * 2 + 28.dp) else getTopicDetailHeight() + 20.dp),
                            contentPadding = PaddingValues(top = 12.dp, start = 12.dp, end = 12.dp)
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
                        if (imageUrlList.size == 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        AsyncImage(
                            model = imageUrlList.last(),
                            contentDescription = "last image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(getTopicDetailHeight())
                                .padding(top = 4.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
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

//                    Text(
//                        text = getDateTimeString(topic.registerTime),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.LightGray
//                    )
                }
            }
        }

        if (showImageViewer.value && selectedImageIndex.value != null && images.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = selectedImageIndex.value!!,
                imageUrls = images.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

@Composable
private fun getTopicDetailHeight(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - 40) / 2).dp
}