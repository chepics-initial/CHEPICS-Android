package com.chepics.chepics.feature.createtopic

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateTopicViewModel: ViewModel() {
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val link: MutableState<String> = mutableStateOf("")
    val imageUris: MutableState<List<Uri>> = mutableStateOf(emptyList())

    fun isActive(): Boolean {
        return true
    }

    fun onTapButton() {

    }
}