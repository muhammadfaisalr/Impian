package com.steadytech.impian.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steadytech.impian.database.dao.DaoImpianCategory
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.database.dao.DaoWishlist
import com.steadytech.impian.database.entity.EntityImpianCategory
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.database.entity.EntityWishlist

@Database(entities = [EntityWishlist::class, EntitySaving::class, EntityImpianCategory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoWishlist() : DaoWishlist

    abstract fun daoSaving() : DaoSaving

    abstract fun daoCategory() : DaoImpianCategory

}