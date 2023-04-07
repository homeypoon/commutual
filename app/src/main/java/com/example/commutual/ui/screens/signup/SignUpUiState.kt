/**
 * This file contains the SignUpUiState data class.
 * It is used to represent the state of the SignUpScreen in the UI.
 */

package com.example.commutual.ui.screens.signup

data class SignUpUiState(
  val email: String = "",
  val password: String = "",
  val repeatPassword: String = ""
)
