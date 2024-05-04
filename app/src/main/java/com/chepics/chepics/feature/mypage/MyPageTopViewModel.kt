package com.chepics.chepics.feature.mypage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.usecase.MyPageTopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageTopViewModel @Inject constructor(private val myPageTopUseCase: MyPageTopUseCase): ViewModel() {
    val user: MutableState<User?> = mutableStateOf(null)
}