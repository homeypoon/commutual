/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.commutual.ui.screens.post_details

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.commutual.CHAT_ID
import com.example.commutual.MESSAGES_SCREEN
import com.example.commutual.POST_DEFAULT_ID
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

    fun initialize(postId: String) {
        launchCatching {
            if (postId != POST_DEFAULT_ID) {
                post.value = storageService.getPost(postId.idFromParameter()) ?: Post()

                val postUser = storageService.getUser(post.value.userId)
                if (postUser != null) {
                    user.value = postUser
                }
            }

//            val chast = storageService.getChatWithPostUserId(post.value.userId) ?: Chat()
//            chat.value.chatId = chast.chatId
        }
    }

    fun resetRequestMessage() {
        uiState.value = uiState.value.copy(requestMessage = "")
    }

    fun onRequestMessageChange(newValue: String) {
        uiState.value = uiState.value.copy(requestMessage = newValue)
    }

    fun getChatId(): String {
        var chatId: String = ""
        Log.d("postdvm chat", "post user${post.value.userId}")
        launchCatching {
            val chat = storageService.getChatWithPostUserId(post.value.userId) ?: Chat()
            chatId = chat.chatId

//            if (chatId = launchCatching {  })
        }
        return chatId
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
//            partnerId = membersId.first { it != accountService.currentUserId }
        )

        launchCatching {
            val chatId = storageService.saveChat(chat.value)
            openScreen("$MESSAGES_SCREEN?$CHAT_ID=$chatId")
            Log.v("Postdetails", "chatid: $chatId")
        }


    }
}
