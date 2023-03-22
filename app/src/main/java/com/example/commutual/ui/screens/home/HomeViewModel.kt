package com.example.commutual.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.commutual.*
import com.example.commutual.model.User
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import com.example.commutual.ui.screens.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
): CommutualViewModel(logService) {

    val upcomingUserTasks = storageService.upcomingUserTasks
//    val user = mutableStateOf(User())


    var uiState = mutableStateOf(ProfileUiState())
        private set

    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {

            storageService.getCurrentUserFlow().collect { user ->
                _user.value = user
            }
        }
    }

//    fun initialize() {
//        launchCatching {
//            user.value = storageService.getUser(accountService.currentUserId) ?: User()
//            uiState.value = uiState.value.copy(
//                totalTasksScheduled = user.value.tasksScheduled,
//                totalTasksCompleted = user.value.tasksCompleted
//            )
//        }
//    }



    // Navigate to EditPost Screen when user clicks on FAB
    fun onAddClick(openScreen: (String) -> Unit) =
//        openScreen("$EDIT_POST_SCREEN?$SCREEN_TITLE={$ST_EDIT_POST}")
    openScreen("$EDIT_POST_SCREEN?$POST_ID=$POST_DEFAULT_ID?$SCREEN_TITLE=${ST_CREATE_POST}")

//    openScreen("$EDIT_POST_SCREEN?$POST_ID={$}?$SCREEN_TITLE=${ST_EDIT_POST}")


}