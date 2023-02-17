package com.example.commutual.ui.screens.chat

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.model.Chat
import com.example.commutual.model.Message
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService

) : CommutualViewModel(logService = logService) {
    val message = mutableStateOf(Message())
    val sender = mutableStateOf(User())
    val partnerObject = mutableStateOf(User())


    val chat = mutableStateOf(Chat())

    fun getMessagesWithUsers(chatId: String): Flow<List<Pair<Message, User>>> {
        return storageService.getMessagesWithUsers(chatId)
    }

    var uiState = mutableStateOf(MessageUiState())
        private set

    private val messageText
        get() = uiState.value.messageText

    private val partnerName
        get() = uiState.value.partner


    fun getSender(chatId: String) {
        launchCatching {

            chat.value = storageService.getChat(chatId) ?: Chat()

            partnerObject.value = storageService.getPartner(chat.value.membersId) ?: User()

            uiState.value = uiState.value.copy(
                partner = partnerObject.value
            )
        }
    }

    fun resetMessageText() {
        uiState.value = uiState.value.copy(messageText = "")
    }

    fun onMessageTextChange(newValue: String) {
        message.value = message.value.copy(text = newValue)
        uiState.value = uiState.value.copy(messageText = newValue)
    }

    fun formatTimestamp(timestamp: Timestamp): String {

        val timeFormatter = SimpleDateFormat(
            "HH:mm:ss d MMM yyyy",
            Locale.getDefault()
        )

        return timeFormatter.format(timestamp.toDate())
    }

    suspend fun onSendClick(chatId: String, focusManager: FocusManager) {

        Log.d("Messageviewmodel", "messagechatid$chatId")

        // Close keyboard
        focusManager.clearFocus()

        // If the user didn't input text for the message, don't save or send the message
        if (messageText.isBlank()) {
            return
        }

            val editedMessage = message.value.copy(
                senderId = accountService.currentUserId)
            storageService.saveMessage(editedMessage, chatId)
            Log.d("Messageviewmodel", "messagechatid$chatId")


        resetMessageText()
    }


}