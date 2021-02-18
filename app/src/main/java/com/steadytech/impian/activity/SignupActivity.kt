package com.steadytech.impian.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.OtpBottomSheetDialogFragment
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textSubtitle : TextView
    private lateinit var textTitleSignin : TextView
    private lateinit var textSignin : TextView

    private lateinit var inputPhoneNumber : TextInputEditText
    private lateinit var inputName : TextInputEditText

    private lateinit var buttonSignup : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_signup)
        this.supportActionBar!!.hide()

        this.init()

        this.buttonSignup.setOnClickListener(this)
        this.textSignin.setOnClickListener(this)
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

        this.inputName = findViewById(R.id.inputName)
        this.inputName.typeface = FontsHelper.INTER.regular(this)

        this.inputPhoneNumber = findViewById(R.id.inputPhoneNumber)
        this.inputPhoneNumber.typeface = FontsHelper.INTER.regular(this)

        this.buttonSignup = findViewById(R.id.buttonSignup)
        this.buttonSignup.typeface = FontsHelper.INTER.regular(this)
    }

    override fun onClick(v: View?) {
        if (v == this.buttonSignup){
            this.sendOtp()
        }
        if (v == this.textSignin){
            this.signIn()
        }
    }

    private fun signIn() {
        startActivity(Intent(this, SigninActivity::class.java))
        finish()
    }

    private fun sendOtp() {
        val phone = this.inputPhoneNumber.text.toString()
        val name = this.inputName.text.toString()

        if (TextUtils.isEmpty(phone)){
            this.inputPhoneNumber.error = this.getString(R.string.phone_number_empty)
            return
        }

        if (TextUtils.isEmpty(name)){
            this.inputName.error = this.getString(R.string.name_empty)
        }

        val bottomSheet = OtpBottomSheetDialogFragment("+62$phone", Constant.MODE.SIGN_UP, name)
        bottomSheet.isCancelable = false
        bottomSheet.show(this.supportFragmentManager, SignupActivity::class.java.simpleName)
    }
}