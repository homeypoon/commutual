

package com.example.commutual.ui.screens.chat

import com.example.commutual.CHAT_ID
import com.example.commutual.MESSAGES_SCREEN
import com.example.commutual.model.Chat
import com.example.commutual.model.User
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

sealed interface ChatUiState {
    data class Success(val partners: Flow<List<User>>) : ChatUiState
    object Error : ChatUiState
    object Loading : ChatUiState
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
) : CommutualViewModel(logService) {

//    val chats: Flow<List<Chat>> = storageService.chats
    val chatsWithUsers = storageService.chatsWithUsers


    fun onChatClick(openScreen: (String) -> Unit, chat: Chat) {
        openScreen("$MESSAGES_SCREEN?$CHAT_ID=${chat.chatId}")
    }

}
