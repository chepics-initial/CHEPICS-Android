package com.chepics.chepics.feature.authentication.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")

    fun loginButtonIsActive(): Boolean {
        return email.value.isNotEmpty() && password.value.isNotEmpty()
    }
}