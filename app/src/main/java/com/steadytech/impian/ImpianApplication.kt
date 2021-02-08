package com.steadytech.impian

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.steadytech.impian.helper.AlarmHelper
import com.steadytech.impian.helper.NotificationHelper
import com.steadytech.impian.manager.MigrationManager
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

class ImpianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        
        val config = RealmConfiguration.Builder()
                .name("impian_database.realm")
                .schemaVersion(0)
                .migration(MigrationManager())
                .build()

        Realm.setDefaultConfiguration(config)

        AlarmHelper.setAlarm(this, 1)

    }
}