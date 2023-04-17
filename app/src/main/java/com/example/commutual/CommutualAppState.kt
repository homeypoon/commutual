/**
 * This file manages the state of the Commutual app, including
 * snackbar messages, navigation, notifications, and alarms.
 */

package com.example.commutual

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.example.commutual.common.snackbar.SnackbarManager
import com.example.commutual.common.snackbar.SnackbarMessage.Companion.toMessage
import com.example.commutual.model.AlarmReceiver
import com.example.commutual.model.AlarmReceiver.Companion.ATTENDANCE
import com.example.commutual.model.AlarmReceiver.Companion.COMPLETION
import com.example.commutual.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

@Stable
class CommutualAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {


    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }


    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    /**
     * Create a notification channel
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Set alarm using an alarm manager
     */
    fun setAlarmManager(
        context: Context,
        attendanceTitleText: String,
        completionTitleText: String,
        contentText: String,
        attendanceCalendar: Calendar,
        completionCalendar: Calendar,
        membersId: Array<String>,
        task: Task,
        chatId: String,
    ) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val attendanceIntent = Intent(context, AlarmReceiver::class.java)

        attendanceIntent.putExtra("title", attendanceTitleText)
        attendanceIntent.putExtra("content", contentText)
        attendanceIntent.putExtra("content", contentText)
        attendanceIntent.putExtra("alarmType", ATTENDANCE)
        attendanceIntent.putExtra("membersId", membersId)
        attendanceIntent.putExtra("chatId", chatId)
        attendanceIntent.putExtra("task", task as Serializable)
        attendanceIntent.action = (System.currentTimeMillis().toString())

        val attendancePendingIntent =
            PendingIntent.getBroadcast(
                context, 0, attendanceIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            attendanceCalendar.timeInMillis,
            attendancePendingIntent
        )

        val completionIntent = Intent(context, AlarmReceiver::class.java)

        completionIntent.putExtra("title", completionTitleText)
        completionIntent.putExtra("content", contentText)
        completionIntent.putExtra("content", contentText)
        completionIntent.putExtra("alarmType", COMPLETION)
        completionIntent.putExtra("membersId", membersId)
        completionIntent.putExtra("chatId", chatId)
        completionIntent.putExtra("task", task as Serializable)
        completionIntent.action = (System.currentTimeMillis().toString())

        val completionPendingIntent =
            PendingIntent.getBroadcast(
                context, 1, completionIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            completionCalendar.timeInMillis,
            completionPendingIntent
        )
    }


    companion object {
        const val CHANNEL_ID = "notificationChannel"
        private const val CHANNEL_NAME = "Commutual Notifications"
        private const val CHANNEL_DESCRIPTION = "Receive notifications from Commutual"
    }
}
