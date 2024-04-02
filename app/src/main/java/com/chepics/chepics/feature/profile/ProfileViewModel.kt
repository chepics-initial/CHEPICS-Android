package com.chepics.chepics.feature.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {
    val selectedTab: MutableState<ProfileTabType> = mutableStateOf(ProfileTabType.TOPICS)

    fun selectTab(type: ProfileTabType) {
        selectedTab.value = type
        when (type) {
            ProfileTabType.TOPICS -> {

            }
            ProfileTabType.COMMENTS -> {
                Log.d("Comment", "selectTab: Comment selected")
            }
        }
    }
}

enum class ProfileTabType {
    TOPICS,
    COMMENTS;

    fun getTitle(): String {
        return when (this) {
            TOPICS -> "トピック"
            COMMENTS -> "コメント"
        }
    }
}