package com.example.remote_no_more

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.example.remote_no_more.R


class GeofenceTransitionsService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)

        if (geofencingEvent?.hasError() == true) {
            Log.e("GeofenceService", "Error in geofencing event.")
            return START_NOT_STICKY
        }

        if (geofencingEvent?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendNotification("Arrived at work!")
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(message: String) {
        val channelId = "work_geofence_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Work Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_work as Int)
            .setContentTitle("Work Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onBind(intent: Intent?) = null
}
