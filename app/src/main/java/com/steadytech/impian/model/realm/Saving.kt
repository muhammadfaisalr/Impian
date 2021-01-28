package com.steadytech.impian.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Saving (

        @PrimaryKey
        var id : Long = 0L,
        var name : String = "",
        var targetId : Long = 0L,
        var amount : Long = 0L,
        var description : String = "",
        var savingDate : String = ""
) : RealmObject()