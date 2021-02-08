package com.steadytech.impian.manager

import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmSchema

class MigrationManager : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        if (oldVersion == 0L){
            schema.get("Wishlist")!!.addField("owner", String::class.java)
        }
    }

}