package com.steadytech.impian.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class EntityWishlist (

        @PrimaryKey(autoGenerate = true)  val id: Long?,
        @ColumnInfo(name =  "name") var name: String?,
        @ColumnInfo(name =  "amount") var amount: Long?,
        @ColumnInfo(name =  "description") var description: String?,
        @ColumnInfo(name =  "category") var category: String?,
        @ColumnInfo(name =  "start_date") var startDate: String?,
        @ColumnInfo(name =  "end_date") var endDate: String?,
        @ColumnInfo(name =  "reminder_interval") var reminderInterval: String?,
        @ColumnInfo(name =  "complete") var isCompleted: Boolean?,
        @ColumnInfo(name = "is_synchronized") var isSynchronized : String?
)