package com.example.commutual.ui.screens.edit_profile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.R
import com.example.commutual.common.snackbar.SnackbarManager
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

    val categoryList = listOf(
        CategoryChips.Academics,
        CategoryChips.Art,
        CategoryChips.Coding,
        CategoryChips.HealthAndWellness,
        CategoryChips.Music,
        CategoryChips.Routine,
        CategoryChips.Sports,
        CategoryChips.Work
    )


    val user = mutableStateOf(User())
    private var uiState = mutableStateOf(EditProfileUiState())
        private set

    private val username
        get() = uiState.value.username

    fun initialize() {
        launchCatching {
            user.value = storageService.getUser(accountService.currentUserId) ?: User()
            uiState.value = uiState.value.copy(username = user.value.username)
        }
    }

    fun onNameChange(newValue: String) {
        user.value = user.value.copy(username = newValue)
        uiState.value = uiState.value.copy(username = user.value.username)
    }

    fun onBioChange(newValue: String) {
        user.value = user.value.copy(bio = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit, focusManager: FocusManager) {

//        user.value = user.value.copy(
//            userId = accountService.currentUserId)

        // Close keyboard
        focusManager.clearFocus()

        // Display error if user didn't input a username
        if (username.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_username_error)
            return
        }

        launchCatching {
            val editedUser = user.value
            if (editedUser.userId.isBlank()) {
                storageService.saveUser(accountService.currentUserId, editedUser)
            } else {
                storageService.updateUser(editedUser)
            }
            uiState.value = uiState.value.copy(username = "")
            popUpScreen()
        }
    }
}