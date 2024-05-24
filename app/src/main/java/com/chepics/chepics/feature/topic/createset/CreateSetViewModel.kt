package com.chepics.chepics.feature.topic.createset

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.CreateSetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateSetViewModel @Inject constructor(private val createSetUseCase: CreateSetUseCase) :
    ViewModel() {
    val setText: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    val topicId: MutableState<String?> = mutableStateOf(null)

    fun onStart(topicId: String) {
        this.topicId.value = topicId
    }

    suspend fun onTapButton(completion: () -> Unit) {
        topicId.value?.let {
            isLoading.value = true
            when (val result = createSetUseCase.createSet(topicId = it, set = setText.value)) {
                is CallResult.Success -> {
                    isLoading.value = false
                    completion()
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    showDialog.value = true
                }
            }
        }
    }
}