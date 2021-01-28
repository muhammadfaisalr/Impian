package com.steadytech.impian.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.steadytech.impian.activity.MainActivity
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*


class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

            if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")) {

                // Quote in Morning
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 8)
                calendar.set(Calendar.MINUTE, 30)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val cur: Calendar = Calendar.getInstance()
                if (cur.after(calendar)) {
                    calendar.add(Calendar.DATE, 1)
                }
                val myIntent = Intent(context, MainActivity::class.java)
                val ALARM1_ID = 10000
                val pendingIntent = PendingIntent.getBroadcast(
                        context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_HALF_DAY, pendingIntent)
            }

    }

}