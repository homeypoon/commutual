package com.example.commutual.model

import com.google.firebase.Timestamp

data class User(
    val username: String = "",
    val bio: String = "",
    val commitCount: Long = 0,
    val tasksScheduled: Long = 0,
    val tasksCompleted: Long = 0,
    val signUpTimestamp: Timestamp = Timestamp.now(),

    val categoryCount: Map<String, Int> = mapOf(
        CategoryEnum.ACADEMICS.name to 0,
        CategoryEnum.ART.name to 0,
        CategoryEnum.CODING.name to 0,
        CategoryEnum.HEALTH_AND_WELLNESS.name to 0,
        CategoryEnum.MUSIC.name to 0,
        CategoryEnum.MISCELLANEOUS.name to 0,
        CategoryEnum.ATHLETICS.name to 0,
        CategoryEnum.WORK.name to 0,
    ),
    val tasksMap: Map<String, String> = emptyMap() // Map<task id, chat id>,
)
