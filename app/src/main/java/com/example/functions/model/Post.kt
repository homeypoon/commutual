package com.example.functions.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue

data class Post(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: FieldValue = FieldValue.serverTimestamp()
)