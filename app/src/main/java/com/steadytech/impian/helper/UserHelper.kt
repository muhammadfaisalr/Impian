package com.steadytech.impian.helper

import android.app.Activity
import android.content.Context

class UserHelper{

    companion object{
        fun isNotAnonymous(activity : Activity) : Boolean{
            val preference = activity.getPreferences(Context.MODE_PRIVATE)
            return preference.getBoolean(Constant.MODE.ANONYMOUS, false)
        }
    }
}