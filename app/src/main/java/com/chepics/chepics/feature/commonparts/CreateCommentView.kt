package com.chepics.chepics.feature.commonparts

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chepics.chepics.R
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@Composable
fun CreateCommentView(
    text: MutableState<String>,
    link: MutableState<String>,
    images: MutableState<List<Uri>>,
    type: CreateCommentType,
    modifier: Modifier = Modifier
) {
    val isEnabled = remember {
        mutableStateOf(true)
    }
    val imagesLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(4)
    ) {
        isEnabled.value = true
        if (it.isEmpty()) return@rememberLauncherForActivityResult
        images.value = it
    }
    val scrollState = rememberScrollState()
    val showLinkTextField = remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            if (showLinkTextField.value) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = link.value,
                        onValueChange = { link.value = it },
                        label = { Text(text = "リンクを入力") },
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = linkTextColor(link = link.value),
                            unfocusedTextColor = linkTextColor(link = link.value),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = ChepicsPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedLabelColor = ChepicsPrimary
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = { Text(text = type.placeholder()) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = ChepicsPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedLabelColor = ChepicsPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (images.value.isNotEmpty()) {
                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                    images.value.forEachIndexed { index, uri ->
                        Box {
                            AsyncImage(
                                model = uri,
                                contentDescription = "topic image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
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
                                        images.value = images.value.filterIndexed { thisIndex, _ ->
                                            thisIndex != index
                                        }
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

                    if (images.value.count() < Constants.TOPIC_IMAGE_COUNT) {
                        Surface(
                            modifier = Modifier
                                .size(80.dp)
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
                                    .size(24.dp)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = "image icon",
                        modifier = Modifier
                            .size(24.dp)
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

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.link),
                        contentDescription = "link icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showLinkTextField.value = true }
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = ChepicsPrimary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.paperplane),
                        contentDescription = "submit icon",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun linkTextColor(link: String): Color {
    if (URLUtil.isValidUrl(link)) {
        return Color.Blue
    }

    if (isSystemInDarkTheme()) {
        return Color.White
    }

    return Color.Black
}

enum class CreateCommentType {
    COMMENT,
    REPLY;

    fun placeholder(): String {
        return when (this) {
            COMMENT -> "コメントを入力"
            REPLY -> "リプライを入力"
        }
    }
}