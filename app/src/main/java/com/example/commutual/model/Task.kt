package com.example.commutual.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Task(
    @DocumentId val taskId: String = "",
    val creatorId: String = "",
    val title: String = "",
    val details: String = "",
    val category: CategoryEnum = CategoryEnum.NONE,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val date: Long = Timestamp.now().toDate().time,

    val startTime: String = "",
    val endTime: String = "",

    val showAttendance: Boolean = false,
    val showAttendanceTimestamp: Timestamp = Timestamp.now(),

    val showCompletion: Boolean = false,
    val showCompletionTimestamp: Timestamp = Timestamp.now(),

    // created task timestamp
    val createTimestamp: Timestamp = Timestamp.now(),
    val taskCompleted: Boolean = false,

    val attendance: Map<String, Int> = emptyMap(),
    val completion: Map<String, Int> = emptyMap(),

    ) : Serializable, Parcelable {
    companion object {
        const val ATTENDANCE_NOT_DONE = 0
        const val ATTENDANCE_YES = 1
        const val ATTENDANCE_NO = 2

        const val COMPLETION_NOT_DONE = 0
        const val COMPLETION_YES = 3
        const val COMPLETION_NO = 4

    }
}
