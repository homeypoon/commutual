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

import com.example.commutual.CHAT_ID
import com.example.commutual.MESSAGES_SCREEN
import com.example.commutual.model.Chat
import com.example.commutual.model.service.LogService
import com.example.commutual.model.service.StorageService
import com.example.commutual.ui.screens.CommutualViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageService,
) : CommutualViewModel(logService) {

  val chats = storageService.chats

  fun onChatClick(openScreen: (String) -> Unit, chat: Chat) {
    openScreen("$MESSAGES_SCREEN?$CHAT_ID=${chat.chatId}")
  }

}
