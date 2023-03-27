package com.example.commutual.ui.screens.profile

data class ProfileUiState(

  val userTotalDays: Long = 0,
  val totalTasksScheduled: Long = 0,
  val totalTasksCompleted: Long = 0,
  val totalTasksMissed: Long = 0,
  val completionRate: String = "N/A" // no tasks yet

)
