/**
 * This data class representing a post model
 */

package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Post(
    @DocumentId val postId: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val category: CategoryEnum = CategoryEnum.ANY,
)