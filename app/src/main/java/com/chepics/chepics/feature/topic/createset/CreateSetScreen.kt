package com.chepics.chepics.feature.topic.createset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.ui.theme.ChepicsPrimary
import com.chepics.chepics.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetScreen(
    topicId: String,
    navController: NavController,
    showBottomNavigation: MutableState<Boolean>,
    viewModel: CreateSetViewModel = hiltViewModel()
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        showBottomNavigation.value = false
        viewModel.onStart(topicId)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        showBottomNavigation.value = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "セットは短く簡潔に設定するのがおすすめです",
                        style = MaterialTheme.typography.labelSmall
                    )

                    TextField(
                        value = viewModel.setText.value,
                        onValueChange = { viewModel.setText.value = it },
                        label = { Text(text = "追加するセットを入力") },
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
                            text = "${viewModel.setText.value.length}",
                            style = MaterialTheme.typography.titleMedium,
                            color = ChepicsPrimary
                        )

                        Text(
                            text = " / ${Constants.SET_COUNT}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider()

                    RoundButton(
                        text = "セットを追加",
                        isActive = viewModel.setText.value.trim().isNotBlank() && viewModel.setText.value.length <= Constants.SET_COUNT,
                        type = ButtonType.Fill,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        viewModel.onTapButton()
                    }
                }
            }
        }
    }
}