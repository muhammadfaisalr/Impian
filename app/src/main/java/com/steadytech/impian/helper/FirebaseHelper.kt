package com.steadytech.impian.helper

import com.google.firebase.auth.FirebaseAuth
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.model.firebase.Wishlist

class FirebaseHelper {
    companion object{
        fun auth() : FirebaseAuth{
            return FirebaseAuth.getInstance()
        }

        fun storeWishlist(entityWishlist: EntityWishlist): Wishlist {
            return Wishlist(
                id = DatabaseHelper.encryptString(entityWishlist.id.toString()),
                amount = DatabaseHelper.encryptString(entityWishlist.amount.toString()),
                category = DatabaseHelper.encryptString(entityWishlist.category),
                completed = DatabaseHelper.encryptString(entityWishlist.isCompleted.toString()),
                description = DatabaseHelper.encryptString(entityWishlist.description),
                startDate = DatabaseHelper.encryptString(entityWishlist.startDate),
                endDate = DatabaseHelper.encryptString(entityWishlist.endDate),
                name = DatabaseHelper.encryptString(entityWishlist.name),
                reminderInterval = DatabaseHelper.encryptString(entityWishlist.reminderInterval)
            )
        }
    }
}