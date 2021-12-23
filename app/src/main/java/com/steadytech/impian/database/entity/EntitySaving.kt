package com.steadytech.impian.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "saving")
data class EntitySaving (
        @PrimaryKey(autoGenerate = true) val id : Long?,
        @ColumnInfo(name = "name") val name : String?,
        @ColumnInfo(name = "wishlist_id") val wishlistID : Long?,
        @ColumnInfo(name = "amount") val amount : Long?,
        @ColumnInfo(name = "description") val description : String?,
        @ColumnInfo(name = "saving_data") val savingDate : String?,
        @ColumnInfo(name = "is_synchronized", defaultValue = "N") val isSynchronized : String?
)