/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.example.commutual.R
import com.example.commutual.common.composable.BasicToolbar
import com.example.commutual.model.User
import com.example.commutual.ui.screens.item.ChatItem


@OptIn(ExperimentalLifecycleComposeApi::class)
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


    var currentPartner by remember { mutableStateOf<User?>(value = null) }
    val coroutineScope = rememberCoroutineScope()
    val chatPartners = mutableMapOf<String, User>()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ) {
        BasicToolbar(title = R.string.chat)

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
