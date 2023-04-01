package com.example.commutual.ui.screens.chat

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import com.example.commutual.*
import com.example.commutual.model.*
import com.example.commutual.model.Task.Companion.ATTENDANCE_NO
import com.example.commutual.model.Task.Companion.ATTENDANCE_YES
import com.example.commutual.model.Task.Companion.COMPLETION_NO
import com.example.commutual.model.Task.Companion.COMPLETION_YES
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
    val currentUser = mutableStateOf(User())
    val partnerObject = mutableStateOf(User())
    val chat = mutableStateOf(Chat())
    val currentUserId = accountService.currentUserId

    var photoUri: MutableState<Uri?> = mutableStateOf(null)

    val messageTabs = enumValues<MessageTabEnum>()
//    var tasks = storageService.tasks


    fun getMessagesAndTasksWithUsers(chatId: String): Flow<List<Pair<Any, User>>> {
        return storageService.getMessagesAndTasksWithUsers(chatId)
    }

    fun getTasks(chatId: String): Flow<List<Task>> {
        return storageService.getTasks(chatId)
    }

    var uiState = mutableStateOf(MessageUiState())
        private set

    val tabIndex
        get() = uiState.value.tabIndex

    private val messageText
        get() = uiState.value.messageText

    private val partnerName
        get() = uiState.value.partner


    fun getSender(chatId: String) {
        launchCatching {

            chat.value = storageService.getChatWithChatId(chatId) ?: Chat()

            currentUser.value = storageService.getUser(accountService.currentUserId) ?: User()

            partnerObject.value = storageService.getPartner(chat.value.membersId) ?: User()

            uiState.value = uiState.value.copy(
                partner = partnerObject.value
            )
        }
    }

    fun setMessageTab(tabIndex: Int) {
        uiState.value = uiState.value.copy(tabIndex = tabIndex)
    }

    fun resetMessageText() {
        uiState.value = uiState.value.copy(messageText = "")
    }

    fun onMessageTextChange(newValue: String) {
        message.value = message.value.copy(text = newValue)
        uiState.value = uiState.value.copy(messageText = newValue)
    }

    fun onAddImageClick(
        launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
        focusManager: FocusManager
    ) {

        // Close keyboard
        focusManager.clearFocus()

        launcher.launch(
            PickVisualMediaRequest(
                //Here we request only photos. Change this to .ImageAndVideo if
                //you want videos too.
                //Or use .VideoOnly if you only want videos.
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )

    }

    fun onCloseImageClick(
        focusManager: FocusManager
    ) {

        // Close keyboard
        focusManager.clearFocus()

        photoUri.value = null
    }

    suspend fun onSendClick(chatId: String, focusManager: FocusManager) {

        // Close keyboard
        focusManager.clearFocus()

        // If the user didn't input text for the message, don't save or send the message
        if (messageText.isBlank()) {
            return
        }

        storageService.saveMessage(message.value, chatId)

    }

    fun onCreatedTaskCLicked() {
        setMessageTab(1)
    }

    fun onConfirmationItemClicked() {
        setMessageTab(1)

    }

    fun onAttendanceYesClicked(task: Task, chatId: String) {

        launchCatching {
            storageService.updateTaskType(task, chatId, ATTENDANCE_YES)
            storageService.incrementCommitCount(ATTENDANCE_YES_POINTS)
        }
    }

    fun onAttendanceNoClicked(task: Task, chatId: String) {
        launchCatching {
            storageService.updateTaskType(task, chatId, ATTENDANCE_NO)
            storageService.incrementCommitCount(ATTENDANCE_NO_POINTS)
            storageService.incrementTasksMissed()
        }
    }


    fun onCompletionYesClicked(task: Task, chatId: String) {
        launchCatching {
            storageService.updateTaskType(task, chatId, COMPLETION_YES)
            storageService.incrementCommitCount(COMPLETION_YES_POINTS)
            storageService.incrementTasksCompleted()
        }
    }

    fun onCompletionNoClicked(task: Task, chatId: String) {
        launchCatching {
            storageService.updateTaskType(task, chatId, COMPLETION_NO)
            storageService.incrementCommitCount(COMPLETION_NO_POINTS)
            storageService.incrementTasksMissed()
        }
    }

    fun onUsernameClick(openScreen: (String) -> Unit) {
        openScreen("$PROFILE_SCREEN?$USER_ID=${partnerObject.value.userId}")
    }

    fun onAddTaskClick(openScreen: (String) -> Unit) {
        openScreen("$EDIT_TASK_SCREEN?$TASK_ID=$TASK_DEFAULT_ID?$CHAT_ID=${chat.value.chatId}?$SCREEN_TITLE=${ST_CREATE_TASK}")
    }


}