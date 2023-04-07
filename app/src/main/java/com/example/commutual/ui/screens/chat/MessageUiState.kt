/**
 * This file contains the MessageUiState data class.
 * It is used to represent the state of the MessageScreen in the UI.
 */

package com.example.commutual.ui.screens.chat

import android.net.Uri
import com.example.commutual.model.User

data class MessageUiState(
    val messageText: String = "",
    val partner: User = User(),
    val tabIndex: Int = 0,
    var photoUri: Uri? = null
)

