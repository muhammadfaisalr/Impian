package com.steadytech.impian.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Wishlist(

    @PrimaryKey
     var id : Long = 0L,
     var name : String = "",
     var amount : Long = 0L,
     var description : String = "",
     var startDate : String = "",
     var endDate : String = "",
     var reminderInterval : String = "",
     var isCompleted : Boolean = false
) : RealmObject()