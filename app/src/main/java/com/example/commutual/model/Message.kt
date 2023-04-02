package com.example.commutual.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val messageId: String = "",
    val senderId: String = "", // user ID
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    // if type = 0, message; if type = 1, reminder message
    val type: Int = 0,
    val photoUri: String? = null,
) {
    companion object {
        const val TYPE_MESSAGE_ONLY = 0
        const val TYPE_IMAGE_ONLY = 1
        const val TYPE_IMAGE_MESSAGE = 2
    }
}
