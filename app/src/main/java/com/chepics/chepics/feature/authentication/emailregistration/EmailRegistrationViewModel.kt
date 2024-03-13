package com.chepics.chepics.feature.authentication.emailregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailRegistrationViewModel: ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)

    fun onTapButton() {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            isLoading.value = false
            if (email.value == "aaa") {
                showAlertDialog.value = true
            } else {
                isCompleted.value = true
            }
        }
    }
}