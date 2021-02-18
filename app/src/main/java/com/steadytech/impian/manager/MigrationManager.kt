package com.steadytech.impian.manager

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmSchema

class MigrationManager : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        if (oldVersion == 0L){
            schema.create("UserLocal")!!
                .addField("uid", String::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
                .addField("name", String::class.java, FieldAttribute.REQUIRED)
                .addField("phoneNumber", String::class.java, FieldAttribute.REQUIRED)
                .addField("motto", String::class.java, FieldAttribute.REQUIRED)
            oldVersion.inc()
        }

        if (oldVersion == 1L){
            schema.get("Wishlist")!!
                .removeField("owner")

            oldVersion.inc()
        }

        if (oldVersion == 2L){
            schema.remove("UserLocal")
            oldVersion.inc()
        }

        if (oldVersion == 3L){
//            schema.create("UserLocal")
//                .addField("uid", String::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
//                .addField("name", String::class.java, FieldAttribute.REQUIRED)
//                .addField("phoneNumber", String::class.java, FieldAttribute.REQUIRED)
//                .addField("motto", String::class.java, FieldAttribute.REQUIRED)

            schema.remove("UserLocal")

            schema.get("Wishlist")!!
//                .removeField("owner")
                .addField("owner", String::class.java, FieldAttribute.REQUIRED)

            oldVersion.inc()
        }

        if (oldVersion == 4L){
            schema.get("Wishlist")!!
                .removeField("owner")


            schema.create("Menu")
                .addField("name", String::class.java, FieldAttribute.REQUIRED)
                .addField("icon", Int::class.java, FieldAttribute.REQUIRED)
                .addField("mode", String::class.java, FieldAttribute.REQUIRED)
        }

        if (oldVersion == 5L){
            schema.get("Menu")!!
                .addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
        }

    }

}