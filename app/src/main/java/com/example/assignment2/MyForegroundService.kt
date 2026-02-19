package com.example.assignment2

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import android.content.pm.ServiceInfo

class MyForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "FOREGROUND_SERVICE_CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager =
                getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification: Notification =
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("The service has started")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val NOTIFICATION_ID = 1
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            val NOTIFICATION_ID = 1
            startForeground(NOTIFICATION_ID, notification)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
