package com.chepics.chepics.feature.explore.top

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.chepics.chepics.feature.explore.viewparts.AutoCompleteView
import com.chepics.chepics.feature.explore.viewparts.ExploreTopBar
import com.chepics.chepics.feature.navigation.Screens

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

    BackHandler {
        focusManager.clearFocus()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            ExploreTopBar(
                focusRequester = focusRequester,
                searchText = searchText,
                showTrailingIcon = searchText.value.isNotEmpty(),
                onTapBackButton = {
                    focusManager.clearFocus()
                    navController.popBackStack()
                }) {
                navController.navigate(Screens.ExploreResultScreen.name + "/${searchText.value}")
            }
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            if (searchText.value.isNotEmpty()) {
                AutoCompleteView(searchText = searchText) {
                    navController.navigate(Screens.ExploreResultScreen.name + "/${searchText.value}")
                }
            }
        }
    }
}