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

        NotificationHelper.createChannelNotification(10044, context)
        val builder = NotificationHelper.showNotification(10044, context)

        with(NotificationManagerCompat.from(context)) {
            notify(10044, builder.build())
        }

    }
}