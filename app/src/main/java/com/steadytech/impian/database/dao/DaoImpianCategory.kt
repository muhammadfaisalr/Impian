package com.steadytech.impian.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.steadytech.impian.database.entity.EntityImpianCategory

@Dao
interface DaoImpianCategory {

    @Query("SELECT * FROM impian_category")
    fun getAll() : List<EntityImpianCategory>

    @Insert
    fun insert(entityImpianCategory: EntityImpianCategory)

}