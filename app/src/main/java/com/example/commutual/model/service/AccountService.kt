

package com.example.commutual.model.service

import com.example.commutual.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val currentUserId: String
  val hasUser: Boolean

  val currentUser: Flow<User>

  suspend fun sendRecoveryEmail(email: String)
  suspend fun createAnonymousAccount()
  suspend fun createAccount(email: String, password: String)
  suspend fun signIn(email: String, password: String)
  suspend fun deleteAccount()
  suspend fun signOut()
}
