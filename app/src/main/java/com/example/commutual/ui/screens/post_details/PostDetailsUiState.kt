/**
 * This file contains the PostDetailsUiState data class.
 * It is used to represent the state of the PostDetailsScreen in the UI.
 */

package com.example.commutual.ui.screens.post_details

import androidx.annotation.StringRes
import com.example.commutual.R

data class PostDetailsUiState(
    val requestMessage: String = "",
    @StringRes val chattingButtonText: Int = R.string.start_chatting,
    val showStartChattingCard: Boolean = false
)
