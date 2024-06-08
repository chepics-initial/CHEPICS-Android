package com.chepics.chepics.feature.authentication.nameregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.NameRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameRegistrationViewModel @Inject constructor(private val nameRegistrationUseCase: NameRegistrationUseCase) :
    ViewModel() {
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
            when (nameRegistrationUseCase.registerName(
                username = username.value,
                fullname = fullname.value
            )) {
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