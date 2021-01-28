package com.steadytech.impian.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.steadytech.impian.receiver.ReminderReceiver
import java.util.*

class AlarmHelper {
    companion object{
        fun setAlarm(context: Context, alarmID : Int){
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 30)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val current = Calendar.getInstance()

            if (current.after(calendar)){
                calendar.add(Calendar.DATE, 1)
            }

            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_HALF_DAY, pendingIntent)
        }
    }
}