/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.makeitso.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.functions.HOME_SCREEN
import com.example.functions.SIGN_UP_SCREEN
import com.example.functions.common.ext.isValidEmail
import com.example.functions.common.snackbar.SnackbarManager
import com.example.functions.model.service.AccountService
import com.example.functions.model.service.LogService
import com.example.functions.screens.login.LoginUiState
import com.example.functions.ui.screens.FunctionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.functions.R.string as AppText


@HiltViewModel
class LoginViewModel @Inject constructor(
  private val accountService: AccountService,
  logService: LogService
) : FunctionsViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (password.isBlank()) {
      SnackbarManager.showMessage(AppText.empty_password_error)
      return
    }

    launchCatching {
      accountService.signIn(email, password)
      openAndPopUp(HOME_SCREEN, HOME_SCREEN)
    }
  }

  fun onRegisterClick(openAndPopUp: (String, String) -> Unit) {
    openAndPopUp(SIGN_UP_SCREEN, SIGN_UP_SCREEN)
  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }
}
