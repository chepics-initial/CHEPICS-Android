package com.chepics.chepics.feature.explore.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.feature.explore.viewparts.ExploreTopBar
import com.chepics.chepics.feature.navigation.Screens
import com.chepics.chepics.ui.theme.ChepicsPrimary

@Composable
fun ExploreTopScreen(navController: NavController) {
    val searchText = remember {
        mutableStateOf("")
    }

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        focusRequester.requestFocus()
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_STOP) {
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            ExploreTopBar(
                focusRequester = focusRequester,
                searchText = searchText,
                showTrailingIcon = searchText.value.isNotEmpty(),
                onTapBackButton = {
                navController.popBackStack()
            }) {
                navController.navigate(Screens.ExploreResultScreen.name + "/${searchText.value}")
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            if (searchText.value.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screens.ExploreResultScreen.name + "/${searchText.value}")
                        }
                        .padding(16.dp)
                ) {
                    Image(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search icon",
                        colorFilter = ColorFilter.tint(color = ChepicsPrimary)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = searchText.value,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "を検索")
                }

                HorizontalDivider()
            }
        }
    }
}