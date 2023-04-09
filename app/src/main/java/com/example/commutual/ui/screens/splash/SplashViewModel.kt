

package com.example.commutual.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.*
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.ConfigurationService
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService,
    private val storageService: StorageService,
) : CommutualViewModel(logService) {
    val showError = mutableStateOf(false)

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(
        openAndPopUp: (String, String) -> Unit,
        openScreen: (String) -> Unit,
    ) {
        showError.value = false
        if (accountService.hasUser) {
            launchCatching {
                if (storageService.hasProfile()) {
                    openAndPopUp(HOME_SCREEN, SPLASH_SCREEN)
                } else {
                    openAndPopUp(HOME_SCREEN, SPLASH_SCREEN)
                    openScreen("$EDIT_PROFILE_SCREEN?$SCREEN_TITLE=$ST_CREATE_PROFILE")
                }
            }
        } else openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
    }

}
