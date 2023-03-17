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

package com.example.commutual.model.service

import com.example.commutual.model.*
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val posts: Flow<List<Post>>
  val userPosts: Flow<List<Post>>
  val chats: Flow<List<Chat>>
//  fun getSender(chatId: String): Flow<List<Message>>

  val chatsWithUsers: Flow<List<Pair<Chat, User>>>
  fun getMessagesWithUsers(chatId: String): Flow<List<Pair<Message, User>>>

  fun getTasksWithUsers(chatId: String): Flow<Pair<List<Pair<Task, User>>, List<Pair<Task, User>>>>
  fun getCompletedTasksWithUsers(chatId: String): Flow<List<Pair<Task, User>>>


  suspend fun searchedPosts(search: String): Flow<List<Post>>
  suspend fun filteredPosts(search: String, category: CategoryEnum): Flow<List<Post>>


//  val messages: Flow<List<Message>>

  // Getter methods
  suspend fun getUser(userId: String): User?
  suspend fun getPartner(membersId: MutableList<String>): User?
  suspend fun getChatWithChatId(chatId: String): Chat?
  suspend fun getChatWithPostUserId(postUserId: String): Chat?



  // User methods
  suspend fun saveUser(userId: String, user: User)
  suspend fun updateUser(user: User)
  suspend fun deleteAllForUser(userId: String)
  suspend fun incrementCommitCount(incrementCommitCount: Long)
  suspend fun incrementTasksScheduled(membersId: Array<String>)
  suspend fun incrementTasksCompleted()

  // Post methods
  suspend fun getPost(postId: String): Post?
  suspend fun savePost(post: Post): String
  suspend fun updatePost(post: Post)
  suspend fun deletePost(postId: String)

  // Task methods
  suspend fun getTask(taskId: String, chatId: String): Task?
  suspend fun saveTask(task: Task, chatId: String): String
  suspend fun updateTask(task: Task, chatId: String, attendanceType: Int?)
  suspend fun updateTaskAM(task: Task, chatId: String, updateType: Int)

  suspend fun deleteTask(taskId: String, chatId: String)

  suspend fun saveChat(chat: Chat): String

  suspend fun saveMessage(message: Message, chatId: String): String
  suspend fun updateMessage(message: Message, chatId: String)

  // return list of filtered posts based on interest

  suspend fun hasProfile(): Boolean


}
