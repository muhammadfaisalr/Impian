package com.steadytech.impian.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var textVersion : TextView

    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_splashscreen)
        this.supportActionBar!!.hide()

        this.init()

        Handler(Looper.myLooper()!!).postDelayed({
            if (this.auth.currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return@postDelayed
            }
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }, 3000L)
    }

    private fun init() {
        this.textVersion = findViewById(R.id.textVersion)
        this.textVersion.typeface = FontsHelper.INTER.medium(this)

        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        this.textVersion.text = "V ${packageInfo.versionName}"

        this.auth = FirebaseAuth.getInstance()
        if (this.auth.currentUser != null){
            this.user = auth.currentUser!!
        }
    }
}