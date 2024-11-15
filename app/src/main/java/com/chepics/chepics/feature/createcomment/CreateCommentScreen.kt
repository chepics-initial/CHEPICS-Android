package com.chepics.chepics.feature.createcomment

import android.net.Uri
import android.webkit.URLUtil
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.NetworkErrorDialog
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommentScreen(
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    navigationItem: CreateCommentNavigationItem,
    viewModel: CreateCommentViewModel = hiltViewModel(),
    completion: () -> Unit
) {
    val scrollState = rememberScrollState()
    val imageScrollState = rememberScrollState()

    val isEnabled = remember {
        mutableStateOf(true)
    }
    val imagesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(4)
    ) {
        isEnabled.value = true
        if (it.isEmpty()) return@rememberLauncherForActivityResult
        viewModel.imageUris.value = it
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        showBottomNavigation.value = false
        viewModel.onStart(
            topicId = navigationItem.topicId,
            setId = navigationItem.setId,
            parentId = navigationItem.parentId,
            type = navigationItem.type,
            replyFor = navigationItem.replyFor
        )
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        showBottomNavigation.value = true
    }

    Box {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Box {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "dismiss button"
                            )
                        }

                        viewModel.type.value?.let { type ->
                            Text(
                                text = type.screenTitle(),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                })
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    viewModel.replyFor.value?.let { reply ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.reply_arrow
                                ),
                                contentDescription = "reply icon",
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${reply.user.fullname}さんに返信",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    viewModel.type.value?.let { type ->
                        TextField(
                            value = viewModel.comment.value,
                            onValueChange = { viewModel.comment.value = it },
                            label = { Text(text = type.placeholder()) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = ChepicsPrimary,
                                focusedIndicatorColor = ChepicsPrimary,
                                focusedLabelColor = ChepicsPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.align(Alignment.End),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${viewModel.comment.value.length}",
                                style = MaterialTheme.typography.titleMedium,
                                color = ChepicsPrimary
                            )

                            Text(
                                text = " / ${Constants.COMMENT_LENGTH}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "リンク",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    TextField(
                        value = viewModel.link.value,
                        onValueChange = { viewModel.link.value = it },
                        label = { Text(text = "リンクを入力") },
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = linkTextColor(viewModel.link),
                            unfocusedTextColor = linkTextColor(viewModel.link),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = ChepicsPrimary,
                            focusedIndicatorColor = ChepicsPrimary,
                            focusedLabelColor = ChepicsPrimary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        Text(
                            text = "画像",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            modifier = Modifier
                                .horizontalScroll(imageScrollState)
                                .padding(vertical = 16.dp)
                        ) {
                            viewModel.imageUris.value.forEachIndexed { index, uri ->
                                Box {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "topic image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (isEnabled.value) {
                                                    isEnabled.value = false
                                                    imagesLauncher.launch(
                                                        PickVisualMediaRequest(
                                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                                        )
                                                    )
                                                }
                                            }
                                    )

                                    Surface(
                                        shape = CircleShape,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .clickable {
                                                viewModel.onTapRemoveIcon(index)
                                            }
                                    ) {
                                        Image(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "remove button",
                                            colorFilter = ColorFilter.tint(Color.White),
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(16.dp)
                                        )
                                    }
                                }

                                if (index != Constants.TOPIC_IMAGE_COUNT - 1) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                }
                            }

                            if (viewModel.imageUris.value.count() < Constants.TOPIC_IMAGE_COUNT) {
                                Surface(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clickable {
                                            if (isEnabled.value) {
                                                isEnabled.value = false
                                                imagesLauncher.launch(
                                                    PickVisualMediaRequest(
                                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                                    )
                                                )
                                            }
                                        }
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Image(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add image",
                                        colorFilter = ColorFilter.tint(Color.LightGray),
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Column(modifier = Modifier.wrapContentHeight()) {
                    HorizontalDivider()

                    RoundButton(
                        modifier = Modifier.padding(16.dp),
                        text = "投稿",
                        isActive = viewModel.isActive(),
                        type = ButtonType.Fill
                    ) {
                        viewModel.onTapSubmitButton {
                            completion()
                            navController.navigateUp()
                        }
                    }
                }
            }
        }

        if (viewModel.isLoading.value) {
            CommonProgressSpinner()
        }

        if (viewModel.showCommentRestrictionDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "このセットではコメントできません") },
                text = { Text(text = "選択しているセット内でのみコメントが可能です") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showCommentRestrictionDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }

        if (viewModel.showReplyRestrictionDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "このトピックではリプライできません") },
                text = { Text(text = "トピック内でセットを選択することでリプライが可能になります") },
                confirmButton = {
                    TextButton(onClick = { viewModel.showReplyRestrictionDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }

        if (viewModel.showNetworkErrorDialog.value) {
            NetworkErrorDialog {
                viewModel.showNetworkErrorDialog.value = false
            }
        }
    }
}

@Composable
fun linkTextColor(link: MutableState<String>): Color {
    if (URLUtil.isValidUrl(link.value)) {
        return Color.Blue
    }

    if (isSystemInDarkTheme()) {
        return Color.White
    }

    return Color.Black
}

data class CreateCommentNavigationItem(
    val topicId: String,
    val setId: String,
    val parentId: String?,
    val type: CreateCommentType,
    val replyFor: Comment? = null
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class CreateCommentNavigationItemNavType : JsonNavType<CreateCommentNavigationItem>() {
    override fun fromJsonParse(value: String): CreateCommentNavigationItem {
        return Gson().fromJson(value, CreateCommentNavigationItem::class.java)
    }

    override fun CreateCommentNavigationItem.getJsonParse(): String {
        return Gson().toJson(this)
    }
}