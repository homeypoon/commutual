package com.example.commutual.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val taskId: String = "",
    val creatorId: String = "",
    val title: String = "",
    val details: String = "",
    val category: CategoryEnum = CategoryEnum.ANY,
    val date: Long = Timestamp.now().toDate().time,
    val startTime: String = "",
    val endTime: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val completed: Boolean = false
)
