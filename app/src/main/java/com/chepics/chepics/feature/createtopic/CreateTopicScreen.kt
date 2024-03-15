package com.chepics.chepics.feature.createtopic

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chepics.chepics.feature.RoundButton
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.chepics.chepics.feature.ButtonType
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopicScreen(navController: NavController, viewModel: CreateTopicViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val imagesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(4)
    ) {
        viewModel.imageUris.value = it
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                TitleView(viewModel = viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionView(viewModel = viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                LinkView(viewModel = viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                ImagesView(viewModel = viewModel) {
                    imagesLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

            Column(modifier = Modifier.wrapContentHeight()) {
                Divider()

                RoundButton(
                    modifier = Modifier.padding(16.dp),
                    text = "投稿",
                    isActive = viewModel.isActive(),
                    type = ButtonType.Fill
                ) {
                    viewModel.onTapButton()
                }
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = ChepicsPrimary,
            focusedIndicatorColor = ChepicsPrimary,
            focusedLabelColor = ChepicsPrimary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ImagesView(viewModel: CreateTopicViewModel, onClick: () -> Unit) {
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
            for (image in viewModel.imageUris.value) {
                AsyncImage(
                    model = image,
                    contentDescription = "topic image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8))
                        .border(1.dp, Color.LightGray)
                        .clickable { onClick.invoke() }
                )
                
                Spacer(modifier = Modifier.width(16.dp))
            }

            if (viewModel.imageUris.value.count() < Constants.TOPIC_IMAGE_COUNT) {
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { onClick.invoke() }
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