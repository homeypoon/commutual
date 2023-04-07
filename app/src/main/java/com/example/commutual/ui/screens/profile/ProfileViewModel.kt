package com.example.commutual.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.*
import com.example.commutual.model.Post
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    logService: LogService,
    val storageService: StorageService,
    val accountService: AccountService
) : CommutualViewModel(logService) {

    var uiState = mutableStateOf(ProfileUiState())
        private set

    val user = mutableStateOf(User())

    fun initialize(userId: String) {

        launchCatching {
            if (userId != TASK_DEFAULT_ID) {
                user.value = storageService.getUser(userId) ?: User()
            } else {
                user.value = storageService.getUser(accountService.currentUserId) ?: User()
            }
            uiState.value = uiState.value.copy(
                totalTasksScheduled = user.value.tasksScheduled,
                totalTasksCompleted = user.value.tasksCompleted,
            )

            calculateCompletionRate(user.value.tasksMissed, user.value.tasksCompleted)
            calculateUserTime()
        }
    }

    private fun calculateUserTime() {
        val timestampDifference: Long =
            Timestamp.now().toDate().time - user.value.signUpTimestamp.toDate().time
        val seconds = timestampDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        uiState.value = uiState.value.copy(
            userTotalDays = days
        )
    }

    private fun calculateCompletionRate(tasksMissed: Long, tasksCompleted: Long) {
        val decimalFormatter = DecimalFormat("##0.00%")
        decimalFormatter.minimumFractionDigits = 0

        val completedTasksRate =
            tasksCompleted.toDouble() / (tasksMissed + tasksCompleted).toDouble()

        if ((tasksMissed + tasksCompleted) != 0L) {
            uiState.value = uiState.value.copy(
                completionRate = decimalFormatter.format(completedTasksRate)
            )
        }
    }

    fun onEditProfileClick(openScreen: (String) -> Unit) {
        openScreen("$EDIT_PROFILE_SCREEN?$SCREEN_TITLE=$ST_EDIT_PROFILE")
    }

    fun onOwnPostClick(openScreen: (String) -> Unit, post: Post) {
        openScreen("$PROFILE_POST_SCREEN?$POST_ID={${post.postId}}")
    }

    fun onOtherPostClick(openScreen: (String) -> Unit, post: Post) {
        openScreen("$POST_DETAILS_SCREEN?$POST_ID={${post.postId}}")
    }

    fun onSettingsClick(openScreen: (String) -> Unit) {
        openScreen(SETTINGS_SCREEN)
    }

}
