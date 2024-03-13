package com.chepics.chepics.feature.authentication.onetimecode

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OneTimeCodeViewModel: ViewModel() {
    val code: MutableState<String> = mutableStateOf("")
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)

    fun onTapButton() {

    }

    fun getCodeFromIndex(index: Int): Char {
        return code.value[index]
    }
}