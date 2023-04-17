

package com.example.commutual.ui.screens.settings

import android.util.Log
import com.example.commutual.SPLASH_SCREEN
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : CommutualViewModel(logService) {


    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            try {
                accountService.signOut()
            } catch (e: Exception) {
                // Handle the exception here, for example by logging an error message
                Log.e("onSignOutClick", "Caught IllegalArgumentException: ${e.message}", e)
            }
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            storageService.deleteAllForUser(accountService.currentUserId)
            accountService.deleteAccount()
            restartApp(SPLASH_SCREEN)
        }
    }

}
