package com.chepics.chepics.feature.authentication.iconregistration

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.auth.IconRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IconRegistrationViewModel @Inject constructor(private val iconRegistrationUseCase: IconRegistrationUseCase) :
    ViewModel() {
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)

    fun onTapRegisterButton() {
        imageUri.value?.let { uri ->
            isLoading.value = true
            viewModelScope.launch {
                when (iconRegistrationUseCase.registerIcon(uri)) {
                    is CallResult.Success -> isLoading.value = false
                    is CallResult.Error -> {
                        isLoading.value = false
                        showAlertDialog.value = true
                    }
                }
            }
        }
    }

    fun onTapSkipButton() {
        viewModelScope.launch {
            iconRegistrationUseCase.skip()
        }
    }
}