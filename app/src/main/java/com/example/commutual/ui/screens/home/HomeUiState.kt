/**
 * This file contains the HomeUiState data class.
 * It is used to represent the state of the HomeScreen in the UI.
 */

package com.example.commutual.ui.screens.home

data class HomeUiState(
  val missedTasksPercentage: String = "0%",
  val completedTasksPercentage: String = "0%",
  )
