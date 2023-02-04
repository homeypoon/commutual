package com.example.commutual.ui.screens.chat

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.Message
import com.example.commutual.ui.screens.item.MessageItem
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
    LaunchedEffect(Unit) { viewModel.initialize(chatId) }

    val sdf = remember { SimpleDateFormat("hh:mm", Locale.ROOT) }

    val messages = viewModel.getMessages(chatId).collectAsStateWithLifecycle(emptyList())
//    val messages = listOf<Message>(Message("s", "hi i'm s"), Message("s", "cool want to be my study buddy??"), Message("x", "hi i'm fdjskfdskjfkldsjlfjdsjdfskljflksjfdkljlkfsdkfjdskljflksdjkldfsjlkdfjlkjdslkjfdjslkfjdslkjfdlskdslkjfldsjdsljklfdsjlkdsdjslkjdsflk3"))

    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        BasicToolbar(
            title = AppText.chat
        )

        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState,
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            items(messages.value, key = { it.messageId }) { message: Message ->
                MessageItem(message, System.currentTimeMillis())
            }
        }

//        ChatInput(
//            onMessageChange = { chat ->
//                messages.add(
//                    ChatMessage(
//                        (messages.size + 1).toLong(),
//                        messageContent,
//                        System.currentTimeMillis()
//                    )
//                )
//
//                coroutineScope.launch {
//                    scrollState.animateScrollToItem(messages.size - 1)
//                }
//
//            }
//        )
    }

}

