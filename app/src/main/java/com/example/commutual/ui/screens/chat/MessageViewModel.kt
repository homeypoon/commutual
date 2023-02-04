package com.example.commutual.ui.screens.chat

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.EDIT_POST_SCREEN
import com.example.commutual.POST_DEFAULT_ID
import com.example.commutual.model.Message
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

    ) : CommutualViewModel(logService = logService) {
    val message = mutableStateOf(Message())
    private var uiState = mutableStateOf(MessageUiState())
        private set

    private val text
        get() = uiState.value.messageText

//    lateinit var messages: Flow<List<Message>>
    fun getMessages(chatId: String): Flow<List<Message>>  {
         return storageService.getMessages(chatId)
     }


    // Navigate to EditPost Screen when user clicks on FAB
    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_POST_SCREEN)

    fun initialize(chatId: String) {
        launchCatching {
            if (chatId != POST_DEFAULT_ID) {
//                message.value = storageService.getPost(chatId.idFromParameter()) ?: Post()
                uiState.value = uiState.value.copy(
                    messageText = message.value.text
                )
//                messages = storageService.getMessages(chatId)
            }
        }
    }

    fun onTextChange(newValue: String) {
        message.value = message.value.copy(text = newValue)
        uiState.value = uiState.value.copy(messageText = newValue)
    }


//    fun onDoneClick(popUpScreen: () -> Unit, focusManager: FocusManager) {
//
//        // Close keyboard
//        focusManager.clearFocus()
//
//        if (title.isBlank()) {
//            SnackbarManager.showMessage(R.string.empty_title_error)
//            return
//        }
//
//        if (description.isBlank()) {
//            SnackbarManager.showMessage(R.string.empty_description_error)
//            return
//        }
//
//        post.value = post.value.copy(
//            userId = accountService.currentUserId
//        )
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
//    }

//
//        post.value = post.value.copy(
//            sender = accountService.currentUserId)
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