package com.example.functions.model

import com.google.firebase.firestore.DocumentId

data class Post(
    @DocumentId val postId: String = "",
    val userId: String = "",
//    val user: User = User(),
    val title: String = "",
    val description: String = "",
//    val timestamp: Any = FieldValue.serverTimestamp()
)