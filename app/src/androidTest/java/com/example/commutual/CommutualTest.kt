

package com.example.commutual

import com.example.commutual.model.Post
import com.example.commutual.model.service.StorageService
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CommutualTest {

  @get:Rule val hilt = HiltAndroidRule(this)

  @Inject lateinit var storage: StorageService
  @Inject lateinit var firestore: FirebaseFirestore

  @Before
  fun setup() {
    hilt.inject()
    runBlocking { firestore.clearPersistence().await() }
  }

  @Test
  fun test() = runTest {
    val newId = storage.savePost(Post(title = "Testing"))
    val result = storage.posts.first()
    assertThat(result).containsExactly(Post(postId = newId, title = "Testing"))
  }
}
