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

    init {
        viewModelScope.launch {
            fetchUser()
        }
    }

    private suspend fun fetchUser() {
        when (val result = myPageTopUseCase.fetchUser()) {
            is CallResult.Success -> user.value = result.data
            is CallResult.Error -> return
        }
    }

    fun logout() {
        viewModelScope.launch {
            myPageTopUseCase.logout()
        }
    }
}