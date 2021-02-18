package com.steadytech.impian.model.realm

import android.view.View
import com.steadytech.impian.R
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Menu(
    @PrimaryKey
    var id : Long = 0L,
    var name: String= "",
    var icon: Int = R.drawable.ic_savings_24,
    var mode: String = ""
) : RealmObject()