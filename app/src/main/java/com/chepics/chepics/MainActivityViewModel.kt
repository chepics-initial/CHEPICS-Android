package com.chepics.chepics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chepics.chepics.usecase.TokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val tokenUseCase: TokenUseCase) :
    ViewModel() {
    val isLoggedIn: MutableState<Boolean> = mutableStateOf(false)

    init {
        observeAccessToken()
    }

    private fun observeAccessToken() {
        tokenUseCase.observeAccessToken().onEach {
            isLoggedIn.value = it.isNotBlank()
        }
    }
}