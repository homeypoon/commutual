

package com.example.commutual.ui.screens.post_details

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.commutual.CHAT_ID
import com.example.commutual.MESSAGES_SCREEN
import com.example.commutual.POST_DEFAULT_ID
import com.example.commutual.R
import com.example.commutual.common.ext.idFromParameter
import com.example.commutual.model.Chat
import com.example.commutual.model.Post
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : CommutualViewModel(logService) {

    val post = mutableStateOf(Post())
    val user = mutableStateOf(User())
    val chat = mutableStateOf(Chat())

    var uiState = mutableStateOf(PostDetailsUiState())
        private set

    private val requestMessage
        get() = uiState.value.requestMessage

    fun setShowRequestMatchCard(showRequestMatchCard: Boolean) {
        uiState.value = uiState.value.copy(showStartChattingCard = showRequestMatchCard)
    }

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()

                val postUser = storageService.getUser(post.value.userId)
                if (postUser != null) {
                    user.value = postUser
                }
                chat.value = storageService.getChatWithPostUserId(post.value.userId) ?: Chat(chatId = "")
                setChattingButtonText()

            }

        }
    }

    private fun setChattingButtonText() {
        if (chat.value.chatId != "") {
            uiState.value = uiState.value.copy(chattingButtonText = R.string.resume_chatting)
        } else {
            uiState.value = uiState.value.copy(chattingButtonText = R.string.start_chatting)
        }

    }

    fun onRequestMessageChange(newValue: String) {
        uiState.value = uiState.value.copy(requestMessage = newValue)
    }

    fun updateStartChattingCard(openScreen: (String) -> Unit) {
        Log.d("postdvm chat", "task user${post.value.userId}")
        launchCatching {
//            val chatObject = storageService.getChatWithPostUserId(task.value.userId)

            Log.d("postdvm chat", "chat object${chat.value.chatId}")
            if (chat.value.chatId != "") {
//                chat.value.chatId = chatObject.chatId
                setShowRequestMatchCard(false)
                openScreen("$MESSAGES_SCREEN?$CHAT_ID=${chat.value.chatId}")
            } else {
                setShowRequestMatchCard(true)
            }
        }
    }

    fun navigateToExistingChat(openScreen: (String) -> Unit) {
        launchCatching {
            val chat = storageService.getChatWithPostUserId(post.value.userId) ?: Chat()
            val chatId = chat.chatId
            Log.v("postdetailsvm", chatId)
            openScreen("$MESSAGES_SCREEN?$CHAT_ID=$chatId")
        }
    }


    fun onStartChattingClick(openScreen: (String) -> Unit) {
        val membersId = mutableListOf(accountService.currentUserId, post.value.userId)

        chat.value = chat.value.copy(
            membersId = membersId
        )

        launchCatching {
            val chatId = storageService.saveChat(chat.value)
            openScreen("$MESSAGES_SCREEN?$CHAT_ID=$chatId")
            Log.v("Postdetails", "chatid: $chatId")
        }


    }
}
