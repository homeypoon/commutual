package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId var chatId: String = "",
    val membersId: MutableList<String> = mutableListOf(),
)