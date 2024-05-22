package com.chepics.chepics.feature.topic.top

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.TopicTopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicTopViewModel @Inject constructor(private val topicTopUseCase: TopicTopUseCase) :
    ViewModel() {
    val topic: MutableState<Topic?> = mutableStateOf(null)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val listImages: MutableState<List<String>?> = mutableStateOf(null)

    fun onStart(topic: Topic) {
        this.topic.value = topic
        viewModelScope.launch {
            fetchTopic()
        }
    }

    private suspend fun fetchTopic() {
        topic.value?.let {
            when (val result = topicTopUseCase.fetchTopic(it.id)) {
                is CallResult.Success -> topic.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        listImages.value = images
    }
}