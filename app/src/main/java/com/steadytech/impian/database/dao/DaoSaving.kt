package com.steadytech.impian.database.dao

import androidx.room.*
import com.steadytech.impian.database.entity.EntitySaving

@Dao
 interface  DaoSaving {

     @Query("SELECT * FROM saving WHERE wishlist_id = :wishlistID")
     fun getAllByWishlistID(wishlistID : Long) : List<EntitySaving>

     @Query("SELECT * FROM saving")
     fun getAll() : List<EntitySaving>

    @Query("SELECT * FROM saving WHERE id = :id")
     fun get(id : Long) : EntitySaving

     @Query("SELECT * FROM saving WHERE wishlist_id = :wishlistID")
     fun getByWishlistID(wishlistID : Long) : EntitySaving?

     @Insert
     fun insert(saving: EntitySaving)

     @Update
     fun update(saving: EntitySaving)

     @Delete
     fun delete(saving: EntitySaving)
}