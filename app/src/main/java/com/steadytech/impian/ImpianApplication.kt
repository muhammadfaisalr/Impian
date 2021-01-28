package com.steadytech.impian

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.steadytech.impian.helper.AlarmHelper
import com.steadytech.impian.helper.NotificationHelper
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.kotlin.where

class ImpianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        AlarmHelper.setAlarm(this, 1)

    }
}