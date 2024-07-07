package com.chepics.chepics.feature.authentication.emailregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.EmailRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailRegistrationViewModel @Inject constructor(
    private val emailRegistrationUseCase: EmailRegistrationUseCase
) : ViewModel() {
    val email: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val showAlreadyAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)

    fun onTapButton() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = emailRegistrationUseCase.createCode(email.value)) {
                is CallResult.Success -> {
                    email.value = result.data
                    isCompleted.value = true
                    isLoading.value = false
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    if (result.exception is InfraException.Server && result.exception.errorCode == APIErrorCode.USED_EMAIL) {
                        showAlreadyAlertDialog.value = true
                        return@launch
                    }
                    showAlertDialog.value = true
                }
            }
        }
    }
}