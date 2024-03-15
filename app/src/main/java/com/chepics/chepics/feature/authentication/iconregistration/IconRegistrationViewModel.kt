package com.chepics.chepics.feature.authentication.iconregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IconRegistrationViewModel: ViewModel() {
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
}