package com.steadytech.impian.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper

class SignupActivity : AppCompatActivity() {

    private lateinit var textTitle : TextView
    private lateinit var textSubtitle : TextView
    private lateinit var textTitleSignin : TextView
    private lateinit var textSignin : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_signup)
        this.supportActionBar!!.hide()

        this.init()
    }

    private fun init() {
        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textSignin = findViewById(R.id.textSignin)
        this.textSignin.typeface = FontsHelper.INTER.medium(this)

        this.textTitleSignin = findViewById(R.id.textTitleSignin)
        this.textTitleSignin.typeface = FontsHelper.INTER.regular(this)

        this.textSubtitle = findViewById(R.id.textSubtitle)
        this.textSubtitle.typeface = FontsHelper.INTER.regular(this)
    }
}