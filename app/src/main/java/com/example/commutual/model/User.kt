package com.example.commutual.model

import com.google.firebase.Timestamp

data class User(
    val username: String = "",
    val bio: String = "",
    val commitCount: Long = 0,
    val tasksScheduled: Long = 0,
    val tasksCompleted: Long = 0,
    val signUpTimestamp: Timestamp = Timestamp.now(),

    val tasksMap: Map<String, String> = emptyMap() // Map<task id, chat id>
)
