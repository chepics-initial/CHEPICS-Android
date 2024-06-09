package com.chepics.chepics.feature.authentication.completion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.usecase.auth.CompleteRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletionViewModel @Inject constructor(private val completeRegistrationUseCase: CompleteRegistrationUseCase) :
    ViewModel() {
    fun onTapSkipButton() {
        viewModelScope.launch {
            completeRegistrationUseCase.skip()
        }
    }
}