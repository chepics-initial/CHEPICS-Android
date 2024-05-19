package com.chepics.chepics.feature.comment

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CommentDetailViewModel : ViewModel() {
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val commentImages: MutableState<List<String>?> = mutableStateOf(null)
    val replyText: MutableState<String> = mutableStateOf("")
    val linkText: MutableState<String> = mutableStateOf("")
    val images: MutableState<List<Uri>> = mutableStateOf(emptyList())
    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        commentImages.value = images
    }
}