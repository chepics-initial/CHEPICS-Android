package com.chepics.chepics.feature.topic.top.viewparts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chepics.chepics.R
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.feature.commonparts.ButtonType
import com.chepics.chepics.feature.commonparts.CommonProgressSpinner
import com.chepics.chepics.feature.commonparts.FooterView
import com.chepics.chepics.feature.commonparts.RoundButton
import com.chepics.chepics.feature.navigation.NavigationParts
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.feature.topic.top.TopicTopViewModel
import com.chepics.chepics.ui.theme.ChepicsPrimary
import kotlinx.coroutines.launch

@Composable
fun TopicSetListView(
    viewModel: TopicTopViewModel,
    navController: NavController,
    showConfirmDialog: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()
    val onBack: () -> Unit = {
        viewModel.showBottomSheet.value = true
    }
    val createSetCompletion: () -> Unit = {
        viewModel.showBottomSheet.value = true
        coroutineScope.launch {
            viewModel.fetchSets()
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            LazyColumn {
                item {
                    Column {
                        Text(
                            text = "set",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Blue
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "セットを選択してください")
                        }
                    }
                }

                when (viewModel.setListUIState.value) {
                    UIState.LOADING -> {
                        item {
                            CommonProgressSpinner(backgroundColor = Color.Transparent)
                        }
                    }

                    UIState.SUCCESS -> {
                        items(viewModel.sets.value) {
                            SetCell(
                                currentSet = viewModel.currentSet.value,
                                set = it,
                                isSelected = it.id == viewModel.selectedSet.value?.id,
                                modifier = Modifier.clickable { viewModel.selectSet(it) }
                            ) {
                                viewModel.showBottomSheet.value = false
                                navController.navigate(Screens.SetCommentScreen.name + "/${it}") {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        NavigationParts.setCommentOnBack,
                                        onBack
                                    )
                                }
                            }
                        }

                        item {
                            LaunchedEffect(Unit) {
                                viewModel.onReachSetFooterView()
                            }

                            FooterView(status = viewModel.setFooterStatus.value)
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "セットを追加する",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Blue,
                                    modifier = Modifier.clickable {
                                        viewModel.topic.value?.let {
                                            viewModel.showBottomSheet.value = false
                                            navController.navigate(Screens.CreateSetScreen.name + "/${it.id}") {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    NavigationParts.createSetOnBack,
                                                    onBack
                                                )
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    NavigationParts.createSetCompletion,
                                                    createSetCompletion
                                                )
                                            }
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    UIState.FAILURE -> {
                        item {
                            Text(text = "投稿の取得に失敗しました。インターネット環境を確認して、もう一度お試しください。")
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        RoundButton(
            text = "選択する", type = ButtonType.Fill,
            isActive = viewModel.isSelectButtonActive(),
            modifier = Modifier.padding(16.dp)
        ) {
            if (viewModel.isSelectButtonActive() && viewModel.currentSet.value != null) {
                showConfirmDialog.value = true
            } else {
                viewModel.onTapSelectButton()
            }
        }

        Box(modifier = Modifier.statusBarsPadding())
    }
}

@Composable
fun SetCell(
    currentSet: PickSet?,
    set: PickSet,
    isSelected: Boolean,
    modifier: Modifier,
    onTapCommentButton: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) Color.Blue else Color.LightGray
        ),
        color = if (isSelected) Color.Blue.copy(0.4f) else Color.Transparent,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (currentSet?.id == set.id) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = ChepicsPrimary
                ) {
                    Text(
                        text = "参加中",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = set.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onTapCommentButton() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "comment icon",
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "${set.commentCount}件",
                        color = Color.Blue,
                        fontSize = 12.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.black_people),
                    contentDescription = "set count icon",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${set.votes}",
                    fontSize = 12.sp
                )
            }

            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    color = Color.White
                ) {
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(fraction = (set.rate / 100).toFloat())
                        .height(32.dp)
                        .align(Alignment.CenterStart),
                    color = Color.Blue
                ) {

                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White.copy(0.8f),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "${set.rate.toInt()}%",
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}