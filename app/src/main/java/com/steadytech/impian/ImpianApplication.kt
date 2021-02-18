package com.steadytech.impian

import android.app.Application
import com.steadytech.impian.helper.*
import com.steadytech.impian.manager.MigrationManager
import com.steadytech.impian.model.realm.Menu
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlin.random.Random

class ImpianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name("impian_database.realm")
                .schemaVersion(6)
                .migration(MigrationManager())
                .build()

        Realm.setDefaultConfiguration(config)

        AlarmHelper.setAlarm(this, 1)

        this.setMenu()
    }

    private fun setMenu() {
        val realm = Realm.getDefaultInstance()
        val menus = ArrayList<Menu>()

        menus.add(Menu(1L,"Pengaturan", R.drawable.ic_settings, Constant.MODE.PROFILE))
        menus.add(Menu(2L,"Edit Akun", R.drawable.ic_edit_24, Constant.MODE.PROFILE))

        for (data in menus){
            realm.executeTransaction{
                val menu = Menu(data.id, data.name, data.icon, data.mode)
                it.copyToRealmOrUpdate(menu)
            }
        }
    }
}
