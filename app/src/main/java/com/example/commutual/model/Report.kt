package com.example.commutual.model

import com.google.firebase.firestore.DocumentId

data class Report(
    @DocumentId val reportId: String = "",
    val reportUserId: String = "",
    val reportType: String = ""
)