package com.steadytech.impian.activity

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class SuccessActivity : AppCompatActivity(), View.OnClickListener, Animator.AnimatorListener {

    lateinit var textTitle : TextView

    lateinit var buttonOk : MaterialButton

    lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_success)

        this.supportActionBar!!.hide()

        this.init()

        this.buttonOk.setOnClickListener(this)
    }

    private fun init() {
        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.bold(this)

        this.lottieAnimationView = findViewById(R.id.lottieAnimation)

        this.buttonOk = findViewById(R.id.buttonOk)
        this.lottieAnimationView.addAnimatorListener(this)
    }

    override fun onClick(v: View?) {
        if (v == this.buttonOk){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationStart(animation: Animator?) {

    }

}