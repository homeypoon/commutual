package com.example.commutual.ui.screens.chat

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.common.composable.MessageInputField
import com.example.commutual.ui.screens.item.MessageItem
import kotlinx.coroutines.launch
import java.util.*
import com.example.commutual.R.string as AppText

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MessagesScreen(
    popUpScreen: () -> Unit,
    chatId: String,
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = hiltViewModel()
) {

//    val messages = remember { mutableStateListOf<Message>() }
    LaunchedEffect(Unit) {
        viewModel.getSender(chatId)
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val sdf = remember { SimpleDateFormat("hh:mm", Locale.ROOT) }
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState

    val messages by viewModel.getMessagesWithUsers(chatId).collectAsState(emptyList())
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
        scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        BasicToolbar(
            title = (stringResource(AppText.chat_with, uiState.partner.username))
        )


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState,
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            items(messages) { (message, user) ->
                MessageItem(message, user)
                { timestamp -> viewModel.formatTimestamp(timestamp) }
            }
        }

        MessageInputField(
            R.string.message,
            uiState.messageText,
            viewModel::onMessageTextChange,
            {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = stringResource(R.string.send)
                )
            },
            {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onSendClick(chatId, focusManager)
                            scrollState.animateScrollToItem(messages.size - 1)
                            Log.v("mscreen", messages.get(messages.size - 1).first.text)
                        }
                    },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = stringResource(R.string.send)
                        )
                    }
                )
            },
            4,
            Modifier
                .padding(2.dp, 8.dp)
                .fillMaxWidth(),
            focusManager
        )

    }

}

