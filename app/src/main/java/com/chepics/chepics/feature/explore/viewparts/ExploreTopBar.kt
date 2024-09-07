package com.chepics.chepics.feature.explore.viewparts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chepics.chepics.ui.theme.ChepicsPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreTopBar(
    focusRequester: FocusRequester,
    searchText: MutableState<String>,
    modifier: Modifier = Modifier,
    showTrailingIcon: Boolean,
    onTapBackButton: () -> Unit,
    keyboardAction: () -> Unit
) {
    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Logo Icon",
                modifier = Modifier.clickable { onTapBackButton() },
                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )

            Surface(
                shape = CircleShape,
                color = Color.LightGray.copy(0.8f),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    textStyle = TextStyle(fontSize = 16.sp),
                    label = { Text(text = "検索") },
                    leadingIcon = {
                        Image(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search icon",
                            colorFilter = ColorFilter.tint(color = Color.Gray)
                        )
                    },
                    trailingIcon = {
                        if (showTrailingIcon) {
                            Image(
                                imageVector = Icons.Default.Close,
                                contentDescription = "search icon",
                                colorFilter = ColorFilter.tint(color = Color.Gray),
                                modifier = Modifier
                                    .clickable { searchText.value = "" }
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = ChepicsPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions {
                        if (searchText.value.isNotEmpty()) {
                            keyboardAction()
                        }
                    }
                )
            }
        }
    })
}