package com.example.commutual.ui.screens.chat

import com.example.commutual.CHAT_ID
import com.example.commutual.MESSAGES_SCREEN
import com.example.commutual.model.Chat
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    logService: LogService,
    storageService: StorageService,

    ) : CommutualViewModel(logService) {

    val chatsWithUsers = storageService.chatsWithUsers


    fun onChatClick(openScreen: (String) -> Unit, chat: Chat) {
        openScreen("$MESSAGES_SCREEN?$CHAT_ID=${chat.chatId}")
    }

}
