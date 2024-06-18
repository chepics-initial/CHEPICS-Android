package com.chepics.chepics.feature.createtopic

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.CreateTopicUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTopicViewModel @Inject constructor(private val createTopicUseCase: CreateTopicUseCase) :
    ViewModel() {
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val link: MutableState<String> = mutableStateOf("")
    val imageUris: MutableState<List<Uri>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)

    fun isActive(): Boolean {
        return title.value.trim().isNotEmpty()
                && title.value.length <= Constants.TOPIC_TITLE_LENGTH
                && description.value.length <= Constants.DESCRIPTION_LENGTH
                && (link.value.isEmpty() || URLUtil.isValidUrl(link.value))
    }

    fun onTapButton(completion: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            when (
                createTopicUseCase.createTopic(
                    title = title.value,
                    link = if (link.value.isNotEmpty() && URLUtil.isValidUrl(link.value)) link.value else null,
                    description = if (description.value.trim()
                            .isNotEmpty()
                    ) description.value else null,
                    images = imageUris.value.ifEmpty { null }
                )
            ) {
                is CallResult.Success -> {
                    isLoading.value = false
                    completion()
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    showAlertDialog.value = true
                }
            }
        }
    }

    fun onTapRemoveIcon(index: Int) {
        imageUris.value = imageUris.value.filterIndexed { thisIndex, _ ->
            thisIndex != index
        }
    }
}