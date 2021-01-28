package com.steadytech.impian.helper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.steadytech.impian.R
import com.steadytech.impian.activity.MainActivity

class NotificationHelper {
    companion object{
        fun createChannelNotification(channelID: Long, context: Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Channel01"
                val descriptionText = "Channel01"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelID.toString(), name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun showNotification(channelID : Long, context : Context) : NotificationCompat.Builder{
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            return NotificationCompat.Builder(context, channelID.toString())
                .setSmallIcon(R.drawable.ic_savings_24)
                .setContentTitle("Apakah Kamu Sudah Menabung Hari Ini?")
                .setContentText("Jangan Lupa Menabung Ya, Agar Impian Mu Cepat Tercapai!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        }
    }
}