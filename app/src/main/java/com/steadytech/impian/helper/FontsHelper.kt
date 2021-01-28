package com.steadytech.impian.helper

import android.app.Activity
import android.graphics.Typeface

class FontsHelper {
    class INTER {
        companion object{
            fun regular(activity: Activity) : Typeface {
                return Typeface.createFromAsset(activity.assets, "font/inter_regular.ttf")
            }
            fun medium(activity: Activity) : Typeface {
                return Typeface.createFromAsset(activity.assets, "font/inter_medium.ttf")
            }
            fun light(activity: Activity) : Typeface {
                return Typeface.createFromAsset(activity.assets, "font/inter_light.ttf")
            }
            fun bold(activity: Activity) : Typeface {
                return Typeface.createFromAsset(activity.assets, "font/inter_bold.ttf")
            }
        }
    }
}