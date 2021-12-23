package com.steadytech.impian.database.dao

import androidx.room.*
import com.steadytech.impian.database.entity.EntityWishlist
import java.net.IDN

@Dao
interface   DaoWishlist {

    @Query("SELECT * FROM wishlist")
    fun getAll() : List<EntityWishlist>

    @Query("SELECT * FROM wishlist WHERE id = :id")
    fun getById(id : Long) : EntityWishlist

    @Query("SELECT * FROM wishlist WHERE complete = :complete")
    fun getUnComplete(complete : Boolean = false) : List<EntityWishlist>

    @Query("SELECT * FROM wishlist WHERE complete = :complete  ")
    fun getComplete(complete : Boolean = true) : List<EntityWishlist>

    @Query("SELECT * FROM wishlist WHERE  name = :name")
    fun getUnCompleteByName(name : String) : List<EntityWishlist>

    @Query("SELECT * FROM wishlist WHERE id = :id")
    fun  get(id : Long) : EntityWishlist

    @Query("SELECT * FROM wishlist WHERE name = :name")
    fun  getByName(name : String) : EntityWishlist

    @Insert
    fun  insert(entityWishlist : EntityWishlist)

    @Update
    fun update(entityWishlist: EntityWishlist);

    @Delete
    fun  delete(entityWishlist: EntityWishlist);

    @Query("UPDATE  wishlist SET complete = 'true'  WHERE  id = :id")
    fun markAsAchieved(id : Long)
}