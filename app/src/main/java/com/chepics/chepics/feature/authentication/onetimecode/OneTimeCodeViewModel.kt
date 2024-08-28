package com.chepics.chepics.feature.authentication.onetimecode

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.OneTimeCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneTimeCodeViewModel @Inject constructor(private val oneTimeCodeUseCase: OneTimeCodeUseCase) :
    ViewModel() {
    val code: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val showInvalidAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)
    val showToast: MutableState<Boolean> = mutableStateOf(false)

    fun onTapButton(email: String) {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = oneTimeCodeUseCase.verifyCode(email = email, code = code.value)) {
                is CallResult.Success -> {
                    isCompleted.value = true
                }

                is CallResult.Error -> {
                    if (result.exception is InfraException.Server && result.exception.errorCode == APIErrorCode.CODE_INCORRECT_OR_EXPIRED) {
                        showInvalidAlertDialog.value = true
                        return@launch
                    }
                    showAlertDialog.value = true
                }
            }
            isLoading.value = false
        }
    }

    fun onTapResendButton(email: String) {
        viewModelScope.launch {
            when (oneTimeCodeUseCase.createCode(email)) {
                is CallResult.Success -> {
                    showToast.value = true
                }

                is CallResult.Error -> return@launch
            }
        }
    }
}