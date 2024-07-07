package com.chepics.chepics.feature.authentication.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode.EMAIL_OR_PASSWORD_INCORRECT
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.LoginUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val showInvalidAlertDialog: MutableState<Boolean> = mutableStateOf(false)

    fun loginButtonIsActive(): Boolean {
        return email.value.isNotEmpty() && password.value.length >= Constants.PASSWORD_LENGTH
    }

    fun onTapLoginButton() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = loginUseCase.login(email = email.value, password = password.value)) {
                is CallResult.Success -> {
                    isLoading.value = false
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    if (result.exception is InfraException.Server && result.exception.errorCode == EMAIL_OR_PASSWORD_INCORRECT) {
                        showInvalidAlertDialog.value = true
                        return@launch
                    }
                    showAlertDialog.value = true
                }
            }
        }
    }
}