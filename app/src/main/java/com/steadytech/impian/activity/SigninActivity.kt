package com.steadytech.impian.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.OtpBottomSheetDialogFragment
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper

class SigninActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textWelcome : TextView
    private lateinit var textSubtitle : TextView
    private lateinit var textSignup : TextView
    private lateinit var textTitleSignup : TextView
    private lateinit var buttonSigninWithGoogle : MaterialButton
    private lateinit var buttonSignin : MaterialButton
    private lateinit var inputNumberPhone : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_signin)

        this.supportActionBar!!.hide()

        this.init()

        this.buttonSignin.setOnClickListener(this)
        this.textSignup.setOnClickListener(this)
    }

    private fun init() {
        this.textWelcome = findViewById(R.id.textWelcome)
        this.textWelcome.typeface = FontsHelper.INTER.medium(this)

        this.textSubtitle = findViewById(R.id.textSubtitle)
        this.textSubtitle.typeface = FontsHelper.INTER.light(this)

        this.textSignup = findViewById(R.id.textSignup)
        this.textSignup.typeface = FontsHelper.INTER.medium(this)

        this.textTitleSignup = findViewById(R.id.textSignup)
        this.textTitleSignup.typeface = FontsHelper.INTER.regular(this)

        this.buttonSigninWithGoogle = findViewById(R.id.buttonSigninWithGoogle)
        this.buttonSigninWithGoogle.typeface = FontsHelper.INTER.regular(this)

        this.buttonSignin = findViewById(R.id.buttonSignin)
        this.buttonSignin.typeface = FontsHelper.INTER.medium(this)

        this.inputNumberPhone = findViewById(R.id.inputNumberPhone)
        this.inputNumberPhone.typeface = FontsHelper.INTER.regular(this)
    }

    override fun onClick(v: View?) {
        if (v == this.buttonSignin){
            this.signin()
        }else if (v == this.textSignup){
            this.signup()
        }
    }

    private fun signup() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    private fun signin() {
        if (this.inputNumberPhone.text.isEmpty()){
            BottomSheets.error(
                this.getString(R.string.phone_number_empty),
                "",
                useTitle = true,
                useSubTitle = true,
                buttonMessage = this.getString(R.string.ok),
                TAG = SigninActivity::class.java.simpleName,
                activity = this
            )
        }else{
            val bottomSheet = OtpBottomSheetDialogFragment("+62${this.inputNumberPhone.text.toString()}")
            bottomSheet.isCancelable = false
            bottomSheet.show(this.supportFragmentManager, "ok")
        }
    }
}