package com.example.functions.model

import com.google.firebase.firestore.DocumentId

data class Post(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
)