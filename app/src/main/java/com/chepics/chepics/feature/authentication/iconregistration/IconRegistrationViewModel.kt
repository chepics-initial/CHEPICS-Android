package com.chepics.chepics.feature.authentication.iconregistration

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IconRegistrationViewModel: ViewModel() {
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
}