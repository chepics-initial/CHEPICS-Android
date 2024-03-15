package com.chepics.chepics.feature.authentication.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)

    fun loginButtonIsActive(): Boolean {
        return email.value.isNotEmpty() && password.value.length >= Constants.PASSWORD_LENGTH
    }

    fun onTapLoginButton() {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            isLoading.value = false
            if (email.value == "aaa") {
                showAlertDialog.value = true
            }
        }
    }
}