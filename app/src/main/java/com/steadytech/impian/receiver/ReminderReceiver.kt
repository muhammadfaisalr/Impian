package com.steadytech.impian.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.steadytech.impian.helper.NotificationHelper
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.kotlin.where


/**
 * Created by MuhammadFaisal |SteadyTech.
 */

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper.createChannelNotification(20, context!!)
        val builder = NotificationHelper.showNotification(20, context)

        with(NotificationManagerCompat.from(context)) {
            notify(20, builder.build())
        }

    }

}