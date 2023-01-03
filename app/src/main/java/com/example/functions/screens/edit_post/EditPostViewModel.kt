package com.example.functions.screens.edit_post

import androidx.compose.runtime.mutableStateOf
import com.example.functions.HOME_SCREEN
import com.example.functions.POST_DEFAULT_ID
import com.example.functions.common.ext.idFromParameter
import com.example.functions.model.Post
import com.example.functions.model.service.LogService
import com.example.functions.model.service.StorageService
import com.example.functions.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService

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

    fun onDoneClick(openScreen: (String) -> Unit) {
        launchCatching {
            val editedPost = post.value
            if (editedPost.id.isBlank()) {
                storageService.save(editedPost)
            } else {
                storageService.update(editedPost)
            }
            openScreen(HOME_SCREEN)
        }
    }
}