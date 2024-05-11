package com.chepics.chepics.feature.editprofile

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.chepics.chepics.domainmodel.User

class EditProfileViewModel: ViewModel() {
    val username: MutableState<String> = mutableStateOf("")
    val fullname: MutableState<String> = mutableStateOf("")
    val bio: MutableState<String> = mutableStateOf("")
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    fun onAppear(user: User) {
        username.value = user.username
        fullname.value = user.fullname
        user.bio?.let {
            bio.value = it
        }
        user.profileImageUrl?.let {
            imageUri.value = it.toUri()
        }
    }
}