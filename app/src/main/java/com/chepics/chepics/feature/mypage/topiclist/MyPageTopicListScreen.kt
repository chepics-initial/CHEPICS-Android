package com.chepics.chepics.feature.mypage.topiclist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.FooterView
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageTopicListScreen(
    navController: NavController,
    viewModel: MyPageTopicListViewModel = hiltViewModel()
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing && viewModel.uiState.value != UIState.LOADING) {
        LaunchedEffect(true) {
            viewModel.fetchSets()
            refreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Logo Icon",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )

                    Text(
                        text = "参加中のセット",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(refreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                when (viewModel.uiState.value) {
                    UIState.LOADING -> {
                        item {
                            CommonProgressSpinner(backgroundColor = Color.Transparent)
                        }
                    }

                    UIState.SUCCESS -> {
                        items(viewModel.sets.value) { pickedSet ->
                            MyPageTopicCell(
                                set = pickedSet,
                                modifier = Modifier.clickable { navController.navigate(Screens.TopicTopScreen.name + "/${pickedSet.topic}") }
                            )
                        }

                        item {
                            LaunchedEffect(Unit) {
                                viewModel.onReachFooterView()
                            }

                            FooterView(status = viewModel.footerStatus.value)
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

            PullToRefreshContainer(
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun MyPageTopicCell(set: MySet, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = ChepicsPrimary),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "topic",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChepicsPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.persons),
                        contentDescription = "votes icon",
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${set.topic.votes}",
                        style = MaterialTheme.typography.labelMedium,
                        color = ChepicsPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = set.topic.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "set",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007AFF)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${set.set.rate.toInt()}%",
                        color = Color(0xFF007AFF)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.blue_persons),
                        contentDescription = "votes icon",
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${set.set.votes}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF007AFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF007AFF),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = set.set.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}