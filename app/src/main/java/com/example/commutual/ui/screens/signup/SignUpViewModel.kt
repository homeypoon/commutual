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

package com.example.commutual.ui.screens.signup

import androidx.compose.runtime.mutableStateOf
import com.example.commutual.*
import com.example.commutual.common.ext.isValidEmail
import com.example.commutual.common.ext.isValidPassword
import com.example.commutual.common.ext.passwordMatches
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.model.service.AccountService
import com.example.commutual.model.service.LogService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.commutual.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val accountService: AccountService,
  logService: LogService
) : CommutualViewModel(logService) {
  var uiState = mutableStateOf(SignUpUiState())
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

  fun onRepeatPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(repeatPassword = newValue)
  }

  fun onLoginClick(openAndPopUp: (String, String) -> Unit) {
    openAndPopUp(LOGIN_SCREEN, LOGIN_SCREEN)
  }

  fun onSignUpClick(openAndPopUp: (String, String) -> Unit, openScreen: (String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (!password.isValidPassword()) {
      SnackbarManager.showMessage(AppText.password_error)
      return
    }

    if (!password.passwordMatches(uiState.value.repeatPassword)) {
      SnackbarManager.showMessage(AppText.password_match_error)
      return
    }


    launchCatching {
      accountService.createAccount(email, password)
      openAndPopUp(HOME_SCREEN, SIGN_UP_SCREEN)
      openScreen("$EDIT_PROFILE_SCREEN?$SCREEN_TITLE=$ST_CREATE_PROFILE")
    }
  }
}
