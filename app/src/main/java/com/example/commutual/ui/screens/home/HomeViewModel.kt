package com.example.commutual.ui.screens.home

import com.example.commutual.*
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
): CommutualViewModel(logService) {

    val upcomingUserTasks = storageService.upcomingUserTasks
//    fun initialize() {
//        launchCatching {
//            user.value = storageService.getUser(accountService.currentUserId) ?: User()
//        }
//    }

    // Navigate to EditPost Screen when user clicks on FAB
    fun onAddClick(openScreen: (String) -> Unit) =
//        openScreen("$EDIT_POST_SCREEN?$SCREEN_TITLE={$ST_EDIT_POST}")
    openScreen("$EDIT_POST_SCREEN?$POST_ID=$POST_DEFAULT_ID?$SCREEN_TITLE=${ST_CREATE_POST}")

//    openScreen("$EDIT_POST_SCREEN?$POST_ID={$}?$SCREEN_TITLE=${ST_EDIT_POST}")


}