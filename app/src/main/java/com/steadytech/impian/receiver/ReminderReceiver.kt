package com.steadytech.impian.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.steadytech.impian.helper.DatabaseHelper
import com.steadytech.impian.helper.NotificationHelper


/**
 * Created by MuhammadFaisal |SteadyTech.
 */

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val db = DatabaseHelper.localDb(context!!)
        val impianLists = db.daoCategory().getAll()

        for (impian in impianLists){
            NotificationHelper.createChannelNotification(20, context)
            val builder = NotificationHelper.showNotification(20, context)

            with(NotificationManagerCompat.from(context)) {
                notify(impian.id!!.toInt(), builder.build())
            }
        }

    }
}