package com.chepics.chepics.feature.mypage.top

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.MyPageTopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageTopViewModel @Inject constructor(private val myPageTopUseCase: MyPageTopUseCase) :
    ViewModel() {
    val user: MutableState<User?> = mutableStateOf(null)
    val uiModel: MutableState<MyPageTopUIModel?> = mutableStateOf(null)

    init {
        myPageTopUseCase.getUserData()?.let {
            uiModel.value = MyPageTopUIModel(
                username = it.username,
                fullname = it.fullname,
                imageUrl = it.imageUrl
            )
        }
        viewModelScope.launch {
            fetchUser()
        }
    }

    private suspend fun fetchUser() {
        when (val result = myPageTopUseCase.fetchUser()) {
            is CallResult.Success -> {
                user.value = result.data
                uiModel.value = MyPageTopUIModel(
                    username = result.data.username,
                    fullname = result.data.fullname,
                    imageUrl = result.data.profileImageUrl.toString()
                )
            }

            is CallResult.Error -> return
        }
    }

    fun logout() {
        viewModelScope.launch {
            myPageTopUseCase.logout()
        }
    }
}

data class MyPageTopUIModel(
    val username: String,
    val fullname: String,
    val imageUrl: String?
)