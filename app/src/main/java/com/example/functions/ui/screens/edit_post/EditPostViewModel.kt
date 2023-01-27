package com.example.functions.ui.screens.edit_post

import androidx.compose.runtime.mutableStateOf
import com.example.functions.POST_DEFAULT_ID
import com.example.functions.common.ext.idFromParameter
import com.example.functions.model.Post
import com.example.functions.model.service.AccountService
import com.example.functions.model.service.LogService
import com.example.functions.model.service.StorageService
import com.example.functions.ui.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

    ) : FunctionsViewModel(logService = logService) {
    val post = mutableStateOf(Post())

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()
            }
        }
    }

    fun onTitleChange(newValue: String) {
        post.value = post.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        post.value = post.value.copy(description = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit, postId: String) {

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
    }
}