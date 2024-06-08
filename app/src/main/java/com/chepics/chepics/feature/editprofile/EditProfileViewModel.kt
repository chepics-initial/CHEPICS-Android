package com.chepics.chepics.feature.editprofile

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.EditProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val editProfileUseCase: EditProfileUseCase) :
    ViewModel() {
    val username: MutableState<String> = mutableStateOf("")
    val fullname: MutableState<String> = mutableStateOf("")
    val bio: MutableState<String> = mutableStateOf("")
    val imageUri: MutableState<Uri?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showAlertDialog: MutableState<Boolean> = mutableStateOf(false)
    private var initialImageUri: Uri? = null

    fun onStart(user: User) {
        username.value = user.username
        fullname.value = user.fullname
        user.bio?.let {
            bio.value = it
        }
        user.profileImageUrl?.let {
            imageUri.value = it.toUri()
            initialImageUri = it.toUri()
        }
    }

    fun onTapButton(completion: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            when (editProfileUseCase.updateUser(
                username = username.value,
                fullname = fullname.value,
                bio = bio.value.ifEmpty { null },
                imageUri = if (initialImageUri == imageUri.value) null else imageUri.value
            )) {
                is CallResult.Success -> {
                    isLoading.value = false
                    completion()
                }

                is CallResult.Error -> {
                    isLoading.value = false
                    showAlertDialog.value = true
                }
            }
        }
    }
}