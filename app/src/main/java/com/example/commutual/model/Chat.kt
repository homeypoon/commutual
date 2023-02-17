package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId val chatId: String = "",
    val membersId: MutableList<String> = mutableListOf(),
    var partnerId: String = "",
)