package com.chepics.chepics.feature.authentication.nameregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NameRegistrationViewModel: ViewModel() {
    val username: MutableState<String> = mutableStateOf("")
    val fullname: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)

    fun isActive(): Boolean {
        return username.value.isNotEmpty() && fullname.value.isNotEmpty()
    }

    fun onTapButton() {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            isLoading.value = false
            if (username.value == "aaa") {
                showAlertDialog.value = true
            } else {
                isCompleted.value = true
            }
        }
    }
}