package com.example.commutual.ui.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.model.Message
import com.example.commutual.model.User
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
    val sender = mutableStateOf(User())

    var uiState = mutableStateOf(MessageUiState())
        private set

    private val messageText
        get() = uiState.value.messageText


    fun getSender() {
        launchCatching {
            sender.value = storageService.getUser(accountService.currentUserId) ?: User()
        }
    }

//    lateinit var messages: Flow<List<Message>>
    fun getMessages(chatId: String): Flow<List<Message>>  {
         return storageService.getMessages(chatId)
     }

    fun resetMessageText() {
        uiState.value = uiState.value.copy(messageText = "")
    }

    fun onMessageTextChange(newValue: String) {
        message.value = message.value.copy(text = newValue)
        uiState.value = uiState.value.copy(messageText = newValue)
    }

    fun onSendClick(chatId: String, focusManager: FocusManager) {

        Log.d("Messageviewmodel", "messagechatid$chatId")

        // Close keyboard
        focusManager.clearFocus()

        // If the user didn't input text for the message, don't save or send the message
        if (messageText.isBlank()) {
            return
        }

        message.value = message.value.copy(
            senderId = accountService.currentUserId
        )

        launchCatching {
            val editedMessage = message.value
            if (editedMessage.messageId.isBlank()) {
                storageService.saveMessage(editedMessage, chatId)
                Log.d("Messageviewmodel", "messagechatid$chatId")
            } else {
                storageService.updateMessage(editedMessage, chatId)
            }
        }
        resetMessageText()
    }


}