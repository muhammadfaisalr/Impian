package com.steadytech.impian.helper

import android.app.Activity
import android.graphics.Typeface
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FontsHelper {
    class INTER {
        companion object{

            fun regular(activity: Activity, vararg views: Any?){
                for (view in views){
                    when (view) {
                        is TextView -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_regular.ttf")
                        }
                        is TextInputEditText -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_regular.ttf")
                        }

                        is MaterialButton -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_regular.ttf")
                        }
                    }
                }
            }

            fun medium(activity: Activity, vararg views: Any?){
                for (view in views){
                    when (view) {
                        is TextView -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_medium.ttf")
                        }
                        is TextInputEditText -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_medium.ttf")
                        }
                        is MaterialButton -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_medium.ttf")
                        }
                    }
                }
            }

            fun bold(activity: Activity, vararg views: Any?){
                for (view in views){
                    when (view) {
                        is TextView -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_bold.ttf")
                        }
                        is TextInputEditText -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_bold.ttf")
                        }
                        is MaterialButton -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_bold.ttf")
                        }
                    }
                }
            }

            fun light(activity: Activity, vararg views: Any?){
                for (view in views){
                    when (view) {
                        is TextView -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_light.ttf")
                        }
                        is TextInputEditText -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_light.ttf")
                        }
                        is MaterialButton -> {
                            view.typeface = Typeface.createFromAsset(activity.assets, "font/inter_light.ttf")
                        }
                    }
                }
            }

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