package com.chepics.chepics.feature.authentication.passwordregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PasswordRegistrationViewModel: ViewModel() {
    val password: MutableState<String> = mutableStateOf("")
    val confirmPassword: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)

    fun isActive(): Boolean {
        return password.value == confirmPassword.value && password.value.length >= Constants.PASSWORD_LENGTH
    }

    fun onTapButton() {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            isLoading.value = false
            if (password.value == "aaaaaaaa") {
                showAlertDialog.value = true
            } else {
                isCompleted.value = true
            }
        }
    }
}