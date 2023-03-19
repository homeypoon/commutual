

package com.example.commutual.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.ui.screens.item.ChatItem


@Composable
@ExperimentalMaterialApi
fun ChatScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {


    val scrollState = rememberScrollState()
//    val chats = viewModel.chats.collectAsStateWithLifecycle(emptyList())
    val chatsWithUsers by viewModel.chatsWithUsers.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ) {
        BasicToolbar(
            title = stringResource(R.string.chat))

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1f, true)
        ) {
            items(chatsWithUsers) { (chat, user) ->
                Surface(modifier = Modifier.clickable {
                    viewModel.onChatClick(openScreen, chat)
                }) {
                    ChatItem(chat = chat, user = user)
                }
            }
//            items(chats.value, key = { it.chatId }) { chat ->
////                val partner = viewModel.getPartner(chat.partnerId)
//
//
//                Surface(modifier = Modifier.clickable {
//                    viewModel.onChatClick(openScreen, chat)
//                }) {
//
//
//                    ChatItem(
//                        chat = chat,
//                        user = user
//                    )
//
//                }
//
//            }

        }
    }
}
