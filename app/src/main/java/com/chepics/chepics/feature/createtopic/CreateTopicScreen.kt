package com.chepics.chepics.feature.createtopic

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.RoundButton
import coil.compose.AsyncImage
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopicScreen(
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: CreateTopicViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
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
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        showBottomNavigation.value = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Box {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "dismiss button"
                        )
                    }

                    Text(
                        text = "新規投稿",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    TitleView(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    DescriptionView(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    LinkView(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    ImagesView(viewModel = viewModel, isEnabled = isEnabled) {
                        isEnabled.value = false
                        imagesLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                        viewModel.onTapButton(completion = {
                            navController.popBackStack()
                        })
                    }
                }
            }

            if (viewModel.isLoading.value) {
                CommonProgressSpinner()
            }
            if (viewModel.showAlertDialog.value) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text(text = "通信エラー") },
                    text = { Text(text = "インターネット環境を確認して、もう一度お試しください。") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.showAlertDialog.value = false }) {
                            Text(text = "OK")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TitleView(viewModel: CreateTopicViewModel) {
    Column {
        Row {
            Text(
                text = "トピック",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = ChepicsPrimary
            ) {
                Text(
                    text = "必須",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(4.dp),
                    color = Color.White
                )
            }
        }

        TextField(
            value = viewModel.title.value,
            onValueChange = { viewModel.title.value = it },
            label = { Text(text = "トピックを入力") },
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
                text = "${viewModel.title.value.length}",
                style = MaterialTheme.typography.titleMedium,
                color = ChepicsPrimary
            )

            Text(
                text = " / ${Constants.TOPIC_TITLE_LENGTH}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DescriptionView(viewModel: CreateTopicViewModel) {
    Column {
        Text(
            text = "説明",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        TextField(
            value = viewModel.description.value,
            onValueChange = { viewModel.description.value = it },
            label = { Text(text = "説明を入力") },
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
                text = "${viewModel.description.value.length}",
                style = MaterialTheme.typography.titleMedium,
                color = ChepicsPrimary
            )

            Text(
                text = " / ${Constants.DESCRIPTION_LENGTH}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LinkView(viewModel: CreateTopicViewModel) {
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
            focusedTextColor = linkTextColor(viewModel = viewModel),
            unfocusedTextColor = linkTextColor(viewModel = viewModel),
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
}

@Composable
fun ImagesView(
    viewModel: CreateTopicViewModel,
    isEnabled: MutableState<Boolean>,
    onClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = "画像",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
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
                                    onClick.invoke()
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
                                onClick.invoke()
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

@Composable
fun linkTextColor(viewModel: CreateTopicViewModel): Color {
    if (URLUtil.isValidUrl(viewModel.link.value)) {
        return Color.Blue
    }

    if (isSystemInDarkTheme()) {
        return Color.White
    }

    return Color.Black
}