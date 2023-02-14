package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId val chatId: String = "",
    val membersId: MutableList<String> = mutableListOf(),
    var partnerId: String = "",
) {
    fun setPartnerId(currentUserId: String): Chat {
        val partnerId = membersId.first { it != currentUserId }
        return Chat(chatId, membersId, partnerId)
    }
}