package com.steadytech.impian.helper

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class UserHelper{

    companion object{
        fun isAnonymous(activity : Activity) : Boolean{
            val preference = activity.getSharedPreferences(Constant.NAME.MODE_IS_ANONYMOUS, Context.MODE_PRIVATE)
            return preference.getBoolean(Constant.MODE.ANONYMOUS, false)
        }

        fun getUid() : String? {
            if (FirebaseAuth.getInstance().currentUser != null){
                return FirebaseAuth.getInstance().currentUser!!.uid
            }
            return null
        }
    }
}