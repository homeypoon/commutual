package com.example.commutual.ui.edit_profile

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

    ) : CommutualViewModel(logService = logService) {
    val user = mutableStateOf(User())


    fun initialize() {
        launchCatching {
            user.value = storageService.getUser(accountService.currentUserId) ?: User()
        }
    }

    fun onNameChange(newValue: String) {
        user.value = user.value.copy(username = newValue)
    }

    fun onBioChange(newValue: String) {
        user.value = user.value.copy(bio = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {

//        user.value = user.value.copy(
//            userId = accountService.currentUserId)

        launchCatching {
            val editedUser = user.value
            if (editedUser.userId.isBlank()) {
                storageService.saveUser(accountService.currentUserId, editedUser)
            } else {
                storageService.updateUser(editedUser)
            }
            popUpScreen()
        }
    }
}