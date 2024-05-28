package com.chepics.chepics.feature.authentication.passwordregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.PasswordRegistrationUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRegistrationViewModel @Inject constructor(private val passwordRegistrationUseCase: PasswordRegistrationUseCase) :
    ViewModel() {
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
            when (passwordRegistrationUseCase.registerPassword(password.value)) {
                is CallResult.Success -> {
                    isLoading.value = false
                    isCompleted.value = true
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    showAlertDialog.value = true
                }
            }
        }
    }
}