package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val messageId: String = "",
    val senderId: String = "", // user ID
    val text: String = "",

//    val profilePhotoUrl: String? = null,
//    val imageUrl: String? = null
//    val timestamp: Int
)
