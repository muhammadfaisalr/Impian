package com.steadytech.impian.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "impian_category")
data class EntityImpianCategory(
    @PrimaryKey(autoGenerate = false) public var id : Long?,
    @ColumnInfo(name = "category_name") public var name : String?,
    @ColumnInfo(name = "image_category") public var image : Int?
)