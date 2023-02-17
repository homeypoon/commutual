package com.example.commutual.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val messageId: String = "",
    val senderId: String = "", // user ID
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),

//    @ServerTimestamp val timestamp: Timestamp = Timestamp.now(),

//    val profilePhotoUrl: String? = null,
//    val imageUrl: String? = null
)
