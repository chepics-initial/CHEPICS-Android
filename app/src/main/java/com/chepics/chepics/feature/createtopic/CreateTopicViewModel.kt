package com.chepics.chepics.feature.createtopic

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chepics.chepics.utils.Constants

class CreateTopicViewModel: ViewModel() {
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val link: MutableState<String> = mutableStateOf("")
    val imageUris: MutableState<List<Uri>> = mutableStateOf(emptyList())

    fun isActive(): Boolean {
        return title.value.trim().isNotEmpty()
                && title.value.length <= Constants.TOPIC_TITLE_LENGTH
                && description.value.length <= Constants.DESCRIPTION_LENGTH
                && (link.value.isEmpty() || URLUtil.isValidUrl(link.value))
    }

    fun onTapButton() {

    }

    fun onTapRemoveIcon(index: Int) {
        imageUris.value = imageUris.value.filterIndexed { thisIndex, _ ->
            thisIndex != index
        }
    }
}