package com.example.commutual.ui.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.commutual.*
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
) : CommutualViewModel(logService) {

    val currentUserTasks = storageService.currentUserTasks

    var uiState = mutableStateOf(HomeUiState())
        private set

    val currentUserId = accountService.currentUserId

    private val currentUser = mutableStateOf(User())

    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {

            storageService.getFlowCurrentUser().collect { user ->
                _user.value = user
            }

        }
    }

    fun initialize() {
        launchCatching {
            currentUser.value = storageService.getUser(accountService.currentUserId) ?: User()

            calculateCompletionRate(currentUser.value.tasksMissed, currentUser.value.tasksCompleted)
        }
    }

    private fun calculateCompletionRate(tasksMissed: Long, tasksCompleted: Long) {

        if ((tasksMissed + tasksCompleted) != 0L) {
            val decimalFormatter = DecimalFormat("##0.00%")
            decimalFormatter.minimumFractionDigits = 0


            val missedTasksPercentage = tasksMissed.toDouble() / (tasksMissed + tasksCompleted).toDouble()
            val completedTasksPercentage = tasksCompleted.toFloat() / (tasksMissed + tasksCompleted).toFloat()

            uiState.value = uiState.value.copy(
                missedTasksPercentage = decimalFormatter.format(missedTasksPercentage),
                completedTasksPercentage = decimalFormatter.format(completedTasksPercentage)
            )
        }
    }



    // Navigate to EditPost Screen when user clicks on FAB
    fun onAddClick(openScreen: (String) -> Unit) =
//        openScreen("$EDIT_POST_SCREEN?$SCREEN_TITLE={$ST_EDIT_POST}")
        openScreen("$EDIT_POST_SCREEN?$POST_ID=$POST_DEFAULT_ID?$SCREEN_TITLE=${ST_CREATE_POST}")

//    openScreen("$EDIT_POST_SCREEN?$POST_ID={$}?$SCREEN_TITLE=${ST_EDIT_POST}")


}