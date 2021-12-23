package com.steadytech.impian.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper

@SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
class Loading(context: Context) : AlertDialog(context) {

    var textLoading : TextView

    init {
        super.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = LayoutInflater.from(context).inflate(R.layout.loading, null)
        this.textLoading = view.findViewById(R.id.textLoading)
        this.textLoading.typeface = FontsHelper.INTER.medium(context as Activity)
    }



    override fun show() {
        super.show()
        super.setContentView(R.layout.loading)
    }
}