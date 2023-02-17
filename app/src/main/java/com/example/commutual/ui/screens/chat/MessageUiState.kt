package com.example.commutual.ui.screens.chat

import com.example.commutual.model.User

data class MessageUiState(
    val messageText: String = "",
    val partner: User = User(),
)

