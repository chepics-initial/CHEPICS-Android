package com.chepics.chepics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.net.RequestHeaderKey
import com.chepics.chepics.usecase.TokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val tokenUseCase: TokenUseCase) :
    ViewModel() {
    val isLoggedIn: MutableState<Boolean> = mutableStateOf(false)
    val isSplashProgress: MutableState<Boolean> = mutableStateOf(true)

    init {
        viewModelScope.launch {
            observeAccessToken()
        }
    }

    private suspend fun observeAccessToken() {
        tokenUseCase.observeAccessToken().collect {
            tokenUseCase.setAccessToken()
            isLoggedIn.value = it.isNotBlank()
            tokenUseCase.setHeaders(mapOf(RequestHeaderKey.AUTHORIZATION_TOKEN.key to "Bearer $it"))
        }
    }
}