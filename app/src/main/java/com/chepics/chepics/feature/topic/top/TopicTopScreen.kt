package com.chepics.chepics.feature.topic.top

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.ImagePager
import com.chepics.chepics.feature.commonparts.NetworkErrorDialog
import com.chepics.chepics.feature.topic.top.viewparts.TopicSetListView
import com.chepics.chepics.feature.topic.top.viewparts.TopicTopContentView
import com.chepics.chepics.feature.topic.top.viewparts.TopicTopDetailView
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicTopScreen(
    navController: NavController,
    navigationItem: TopicTopNavigationItem,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: TopicTopViewModel = hiltViewModel()
) {
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    val showConfirmDialog = remember {
        mutableStateOf(false)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        viewModel.onStart(topicId = navigationItem.topicId, rootTopic = navigationItem.topic)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (viewModel.status.value) {
                        TopicTopStatus.TOP -> {
                            TopicTopContentView(
                                viewModel,
                                navController,
                                onTapShowSetList = {
                                    viewModel.showBottomSheet.value = true
                                },
                                onTapImage = { index, images ->
                                    viewModel.onTapImage(index, images)
                                    showImageViewer.value = true
                                }
                            )
                        }

                        TopicTopStatus.DETAIL -> {
                            TopicTopDetailView(
                                navController = navController,
                                viewModel = viewModel
                            ) { index, images ->
                                viewModel.onTapImage(index, images)
                                showImageViewer.value = true
                            }
                        }

                        TopicTopStatus.LOADING -> {
                            CommonProgressSpinner(backgroundColor = Color.Transparent)
                        }

                        TopicTopStatus.FAILURE -> {
                            Text(
                                text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                if (viewModel.showBottomSheet.value) {
                    viewModel.fetchSets()
                    ModalBottomSheet(onDismissRequest = {
                        viewModel.showBottomSheet.value = false
                    }) {
                        TopicSetListView(
                            viewModel = viewModel,
                            navController = navController,
                            showConfirmDialog = showConfirmDialog
                        )
                    }
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }

        if (viewModel.showAlert.value) {
            NetworkErrorDialog {
                viewModel.showAlert.value = false
                viewModel.showBottomSheet.value = true
            }
        }

        if (showConfirmDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "このセットを選択しますか？") },
                text = { Text(text = "既に他のセットを選択しているため、そのセット内で行ったコメントやいいねは削除されます。") },
                dismissButton = {
                    TextButton(onClick = {
                        showConfirmDialog.value = false
                    }) {
                        Text(text = "キャンセル")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDialog.value = false
                        viewModel.onTapSelectButton()
                    }) {
                        Text(text = "選択する")
                    }
                }
            )
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.listImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.listImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}

data class TopicTopNavigationItem(
    val topicId: String,
    val topic: Topic?
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class TopicTopNavigationItemNavType : JsonNavType<TopicTopNavigationItem>() {
    override fun fromJsonParse(value: String): TopicTopNavigationItem {
        return Gson().fromJson(value, TopicTopNavigationItem::class.java)
    }

    override fun TopicTopNavigationItem.getJsonParse(): String {
        return Gson().toJson(this)
    }
}