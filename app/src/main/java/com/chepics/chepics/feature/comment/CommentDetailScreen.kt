package com.chepics.chepics.feature.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.feature.commonparts.CommentCell
import com.chepics.chepics.feature.commonparts.CommentType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.ImagePager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDetailScreen(
    comment: Comment,
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: CommentDetailViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val showImageViewer = remember {
        mutableStateOf(false)
    }

    Box {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Logo Icon",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                })
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(scrollState)

            ) {
                CommentCell(comment = comment, type = CommentType.DETAIL) { index ->
                    comment.images?.let { images ->
                        viewModel.onTapImage(index = index, images = images.map { image ->
                            image.url
                        })
                        showImageViewer.value = true
                    }
                }

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reply",
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "4件の返信",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.LightGray
                    )
                }
            }
        }

        if (showImageViewer.value && viewModel.selectedImageIndex.value != null && viewModel.commentImages.value != null) {
            showBottomNavigation.value = false
            ImagePager(
                index = viewModel.selectedImageIndex.value!!,
                imageUrls = viewModel.commentImages.value!!
            ) {
                showImageViewer.value = false
                showBottomNavigation.value = true
            }
        }
    }
}