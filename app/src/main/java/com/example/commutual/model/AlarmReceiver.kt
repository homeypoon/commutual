package com.example.commutual.model

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.commutual.CommutualAppState
import com.example.commutual.R


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val content = intent?.getStringExtra("content")

        if (context != null) {

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
                if (builder != null) {
                    notify(0, builder.build())
                }
            }
        }


    }

    companion object {
        private const val REQUEST_PERMISSION_PHONE_STATE = 1
    }
}