    package com.steadytech.impian.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.OtpBottomSheetDialogFragment
import com.steadytech.impian.custom.Loading
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
    private lateinit var buttonSigninAnonymously : MaterialButton

    private lateinit var inputNumberPhone : EditText

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_signin)

        this.supportActionBar!!.hide()

        this.init()

        this.buttonSignin.setOnClickListener(this)
        this.textSignup.setOnClickListener(this)
        this.buttonSigninAnonymously.setOnClickListener(this)
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

        this.buttonSigninAnonymously = findViewById(R.id.buttonSigninAnonymously)
        this.buttonSigninAnonymously.typeface = FontsHelper.INTER.regular(this)

        this.buttonSignin = findViewById(R.id.buttonSignin)
        this.buttonSignin.typeface = FontsHelper.INTER.medium(this)

        this.inputNumberPhone = findViewById(R.id.inputNumberPhone)
        this.inputNumberPhone.typeface = FontsHelper.INTER.regular(this)

        this.auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        if (v == this.buttonSignin){
            this.signin()
        }else if (v == this.textSignup){
            this.signup()
        }else if (v == this.buttonSigninAnonymously){
            this.signinAnonymously()
        }
    }

    private fun signinAnonymously() {

        val loading = Loading(this)
        loading.setCancelable(false)
        loading.show()

        this.auth.signInAnonymously()
            .addOnCompleteListener {
                loading.dismiss()

                val preferences = this.getSharedPreferences(Constant.NAME.MODE_IS_ANONYMOUS, Context.MODE_PRIVATE)
                with(preferences.edit()){
                    putBoolean(Constant.MODE.ANONYMOUS, true)
                    apply()
                }

                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnCompleteListener {
                loading.dismiss()
                BottomSheets.error(
                    this.getString(R.string.ups),
                    this.getString(R.string.something_wrong),
                    useTitle = true,
                    useSubTitle = true,
                    buttonMessage = this.getString(R.string.ok),
                    TAG = SigninActivity::class.java.simpleName,
                    activity = this
                )
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
                useSubTitle = false,
                buttonMessage = this.getString(R.string.ok),
                TAG = SigninActivity::class.java.simpleName,
                activity = this
            )
        }else{
            val phone = this.inputNumberPhone.text.toString()

            if (TextUtils.isEmpty(phone)){
                this.inputNumberPhone.error = this.getString(R.string.phone_number_empty)
                return
            }

            val bottomSheet = OtpBottomSheetDialogFragment("+62$phone", Constant.MODE.SIGN_IN, "")
            bottomSheet.isCancelable = false
            bottomSheet.show(this.supportFragmentManager, SigninActivity::class.java.simpleName)
        }
    }

    fun createGoals(v : View){
        BottomSheets.description(
            R.drawable.ic_target,
            this.getString(R.string.create_goals),
            this.getString(R.string.description_create_goals),
            this,
            SigninActivity::class.java.simpleName
        )
    }
    fun dailyReminder(v : View){
        BottomSheets.description(
            R.drawable.ic_notification,
            this.getString(R.string.daily_reminder),
            this.getString(R.string.description_daily_reminder),
            this,
            SigninActivity::class.java.simpleName
        )
    }

    fun free(v : View){
        BottomSheets.description(
            R.drawable.ic_free,
            this.getString(R.string.free),
            this.getString(R.string.description_free),
            this,
            SigninActivity::class.java.simpleName   
        )
    }
}