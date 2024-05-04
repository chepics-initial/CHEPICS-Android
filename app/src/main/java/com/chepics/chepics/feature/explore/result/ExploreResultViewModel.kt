package com.chepics.chepics.feature.explore.result

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.ExploreResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreResultViewModel @Inject constructor(private val exploreResultUseCase: ExploreResultUseCase): ViewModel() {
    val searchText: MutableState<String> = mutableStateOf("")
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val userUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val comments: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val users: MutableState<List<User>> = mutableStateOf(emptyList())
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val userScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val searchImages: MutableState<List<String>?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            fetchTopics()
        }
    }

    fun onAppear(searchText: String) {
        this.searchText.value = searchText
    }

    fun selectTab(index: Int) {
        selectedTab.value = index
        when (index) {
            0 -> {
                if (topicUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchTopics()
                    }
                }
            }

            1 -> {
                if (commentUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchComments()
                    }
                }
            }

            2 -> {
                if (userUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchUsers()
                    }
                }
            }
        }
    }

    suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        when (val result = exploreResultUseCase.fetchTopics(searchText.value)) {
            is CallResult.Success -> {
                topics.value = result.data
                topicUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                topicUIState.value = UIState.FAILURE
            }
        }
    }

    suspend fun fetchComments() {
        if (commentUIState.value != UIState.SUCCESS) {
            commentUIState.value = UIState.LOADING
        }
        when (val result = exploreResultUseCase.fetchComments(searchText.value)) {
            is CallResult.Success -> {
                comments.value = result.data
                commentUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                commentUIState.value = UIState.FAILURE
            }
        }
    }

    suspend fun fetchUsers() {
        if (userUIState.value != UIState.SUCCESS) {
            userUIState.value = UIState.LOADING
        }

        when (val result = exploreResultUseCase.fetchUsers(searchText.value)) {
            is CallResult.Success -> {
                users.value = result.data
                userUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                userUIState.value = UIState.FAILURE
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        searchImages.value = images
    }
}

data class SearchTabItem(
    val title: String
)

val searchTabItems = listOf(SearchTabItem(title = "トピック"), SearchTabItem(title = "コメント"), SearchTabItem(title = "ユーザー"))