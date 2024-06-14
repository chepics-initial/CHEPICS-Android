package com.chepics.chepics.feature.topic.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetCommentScreen(
    set: PickSet,
    navController: NavController,
    viewModel: SetCommentViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onStart(set)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = "chat icon",
                            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "コメント${selectedSet.commentCount}件",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

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

                when (viewModel.uiState.value) {
                    UIState.LOADING -> {
                        CommonProgressSpinner(backgroundColor = Color.Transparent)
                    }

                    UIState.SUCCESS -> {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(viewModel.comments.value) { comment ->
                                CommentCell(
                                    comment = comment,
                                    type = CommentType.SET,
                                    modifier = Modifier.clickable {
                                        navController.navigate(Screens.SetCommentDetailScreen.name + "/${selectedSet}/${comment}") {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "onBack",
                                                onBack
                                            )
                                        }
                                    },
                                    onTapImage = {},
                                    onTapUserInfo = {})
                            }
                        }
                    }

                    UIState.FAILURE -> {
                        Text(
                            text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}