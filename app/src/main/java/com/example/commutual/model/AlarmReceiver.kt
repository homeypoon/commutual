/**
 * A BroadcastReceiver that handles the alarms for attendance and completion of tasks to prompt
 * timely notifications to the user
 */

package com.example.commutual.model

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.commutual.CommutualAppState
import com.example.commutual.R
import com.example.commutual.model.service.StorageService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var storageService: StorageService

    override fun onReceive(context: Context, intent: Intent?) {

        // Retrieve data from intent
        val title = intent?.getStringExtra("title")
        val content = intent?.getStringExtra("content")
        val chatId = intent?.getStringExtra("chatId")
        val membersId = intent?.getStringArrayExtra("membersId")
        val alarmType = intent?.getIntExtra("alarmType", COMPLETION)

        val task: Task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra("task", Task::class.java) ?: Task()
        } else {
            intent?.getSerializableExtra("task") as? Task ?: Task()
        }

        if (alarmType == ATTENDANCE) {
            // Update Firebase accordingly if it's an attendance alarm
            if (chatId != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    storageService.updateTaskAM(task, chatId, ATTENDANCE)

                    if (membersId != null) {
                        storageService.incrementTasksScheduled(membersId)
                        storageService.incrementCategoryCount(membersId, task.category)
                    }
                }
            }
        } else if (alarmType == COMPLETION) {
            // Update Firebase accordingly if it's a completion alarm
            if (chatId != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    storageService.updateTaskAM(task, chatId, COMPLETION)
                }
            }
        }

        // Create notification builder to create a notification with relevant properties
        val builder =
            NotificationCompat.Builder(context, CommutualAppState.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        // Prompt notification if Commutual has permission
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(0, builder.build())
        }
    }

    companion object {
        const val ATTENDANCE = 1
        const val COMPLETION = 2
    }
}