package com.example.commutual.model

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
class AlarmReceiver(

) : BroadcastReceiver() {

    @Inject
    lateinit var storageService: StorageService

    override fun onReceive(context: Context, intent: Intent?) {

        Log.d("AM receiver", "onReceive called")
        val title = intent?.getStringExtra("title")
        val content = intent?.getStringExtra("content")
        val chatId = intent?.getStringExtra("chatId")
        val membersId = intent?.getStringArrayExtra("membersId")


        val alarmType = intent?.getIntExtra("alarmType", COMPLETION)

        Log.d("alarm receiver", "alarm type:$alarmType")
        Log.d("before am task", "task:${intent?.getSerializableExtra("task")}")

        val task: Task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra("task", Task::class.java) as? Task ?: Task()
        } else {
//            intent?.getSerializableExtra("task") as Task
            intent?.getSerializableExtra("task") as? Task ?: Task()

        }

        Log.d("alarm r", "title:$title content: $content")
        Log.d("alarm r task", "task:$task")


        if (alarmType == ATTENDANCE) {

            if (chatId != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    storageService.updateTaskAM(task, chatId, ATTENDANCE)

                    if (membersId != null) {
                        storageService.incrementTasksScheduled(membersId)
                        storageService.incrementCategoryCount(membersId, task.category)
                    }

                    Log.d("attendance alarm r task", "chatid: $chatId")

                    Log.d("attendance alarm r task", "task sent")
                }
            }
        } else if (alarmType == COMPLETION) {
            Log.d("comple. alarm r task", "type:$alarmType")

            if (chatId != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    storageService.updateTaskAM(task, chatId, COMPLETION)

                    Log.d("comple. alarm r task", "chatid: $chatId")

                    Log.d("comple. alarm r task", "task sent")
                }

            }
        }


        val builder =
            NotificationCompat.Builder(context, CommutualAppState.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_home)
                //            .setSmallIcon(R.drawable.ic_commutual)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)


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
        const val NONE = 0
        const val ATTENDANCE = 1
        const val COMPLETION = 2
    }
}