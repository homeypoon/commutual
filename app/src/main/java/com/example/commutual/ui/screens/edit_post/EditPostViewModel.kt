package com.example.commutual.ui.screens.edit_post

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.POST_DEFAULT_ID
import com.example.commutual.R
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.model.Post
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

    ) : CommutualViewModel(logService = logService) {
    val post = mutableStateOf(Post())
    private var uiState = mutableStateOf(EditPostUiState())
        private set

    private val title
        get() = uiState.value.postTitle
    private val description
        get() = uiState.value.postDescription

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()
                uiState.value = uiState.value.copy(
                    postTitle = post.value.title,
                    postDescription = post.value.description)
            }
        }
    }

    fun onTitleChange(newValue: String) {
        post.value = post.value.copy(title = newValue)
        uiState.value = uiState.value.copy(postTitle = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        post.value = post.value.copy(description = newValue)
        uiState.value = uiState.value.copy(postDescription = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit, focusManager: FocusManager) {

        // Close keyboard
        focusManager.clearFocus()

        if (title.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_title_error)
            return
        }

        if (description.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_description_error)
            return
        }

        post.value = post.value.copy(
            userId = accountService.currentUserId)

        launchCatching {
            val editedPost = post.value
            if (editedPost.postId.isBlank()) {
                storageService.savePost(editedPost)
            } else {
                storageService.updatePost(editedPost)
            }
            popUpScreen()
        }
//
//        post.value = post.value.copy(
//            userId = accountService.currentUserId)
//        post.value = post.value.copy(postId = postId)
//
//        launchCatching {
//            val editedPost = post.value
//            if (editedPost.postId.isBlank()) {
//                storageService.savePost(editedPost)
//            } else {
//                storageService.updatePost(editedPost)
//            }
//            popUpScreen()
//        }
    }
}