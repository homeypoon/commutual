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

package com.example.functions.model.service

import com.example.functions.model.Post
import com.example.functions.model.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val posts: Flow<List<Post>>
  val userPosts: Flow<List<Post>>

  suspend fun hasUsername(): Boolean

  suspend fun getPost(postId: String): Post?
  suspend fun getUser(userId: String): User?

  // Post
  suspend fun savePost(post: Post): String
  suspend fun updatePost(post: Post)
  suspend fun deletePost(postId: String)

//  suspend fun saveUser(user: User): String
  suspend fun saveUser(userId: String, user: User): Unit

  suspend fun updateUser(user: User)

  suspend fun deleteAllForUser(userId: String)
}
