package com.example.commutual.ui.screens.edit_post

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.POST_DEFAULT_ID
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.model.Post
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

    ) : CommutualViewModel(logService = logService) {
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