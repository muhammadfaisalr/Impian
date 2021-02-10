package com.steadytech.impian.bottomsheet

import android.content.Intent
import android.graphics.fonts.Font
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.philliphsu.bottomsheetpickers.date.TextViewWithCircularIndicator
import com.poovam.pinedittextfield.SquarePinField
import com.steadytech.impian.R
import com.steadytech.impian.activity.MainActivity
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.FontsHelper
import java.util.concurrent.TimeUnit

class OtpBottomSheetDialogFragment(private val phoneNumber : String) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textResend : TextView

    private lateinit var buttonSignin : MaterialButton

    private lateinit var inputCode : SquarePinField

    private lateinit var activityCompat: AppCompatActivity

    private lateinit var auth : FirebaseAuth

    private lateinit var storedVerificationID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_otp_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.init(view)

        this.sendOtp()

        this.buttonSignin.setOnClickListener(this)
    }


    private fun init(view: View) {
        this.activityCompat = this.activity as AppCompatActivity

        this.auth = FirebaseAuth.getInstance()

        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this.activityCompat)
        this.textTitle.text = this.getString(R.string.otp_sended) + " +62 " + phoneNumber

        this.textResend = view.findViewById(R.id.textResend)
        this.textResend.typeface = FontsHelper.INTER.regular(this.activityCompat)

        this.buttonSignin = view.findViewById(R.id.buttonSignin)
        this.buttonSignin.typeface = FontsHelper.INTER.regular(this.activityCompat)

        this.inputCode = view.findViewById(R.id.inputCode)
        this.inputCode.typeface = FontsHelper.INTER.medium(this.activityCompat)
    }


    private fun sendOtp() {
        this.auth.setLanguageCode("id")

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("VerificationComplete", "onVerificationComplete: $p0")
                this@OtpBottomSheetDialogFragment.signinWithCredential(p0)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                var message = "Ada Sesuatu Yang Salah, Hanya Itu Yang Kami Tau."

                Log.d("VerificationFail", "onVerificationFail: $e")

                if (e is FirebaseAuthInvalidCredentialsException){
                    Log.d("VerificationFail", "onVerificationFail: $e")
                    message = "Tidak Dapat Mengirim OTP Karena Kesalahan Kredensial"
                }else if (e is FirebaseTooManyRequestsException){
                    Log.d("VerificationFail", "onVerificationFail: $e")
                    message = "Terlalu Banyak Permintaan Pengiriman Kode OTP. Anda Dapat Memintanya Kembali Dalam 5 Menit."
                }

                BottomSheets.error(
                        this@OtpBottomSheetDialogFragment.getString(R.string.ups),
                        message,
                        true,
                        true,
                        this@OtpBottomSheetDialogFragment.getString(R.string.ok),
                        this@OtpBottomSheetDialogFragment.tag.toString(),
                        this@OtpBottomSheetDialogFragment.activityCompat
                )
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.d("onCodeSent", "OTP Delivered")

                this@OtpBottomSheetDialogFragment.storedVerificationID = p0
            }
        }

        val options = PhoneAuthOptions.newBuilder(this.auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this.activityCompat)
                .setCallbacks(callbacks)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signinWithCredential(credential: PhoneAuthCredential) {
        this.auth.signInWithCredential(credential)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Log.d(this.tag.toString(), "Sign In With Credential Success")
                        startActivity(Intent(this.activityCompat, MainActivity::class.java))
                        this.activityCompat.finish()
                    }else{
                        var message = "Gagal Masuk Dengan Kredensial, Silahkan Mulai Ulang Aplikasi"

                        Log.d(this.tag.toString(), "Sign In With Credential Failed")

                        if (task.exception is FirebaseAuthInvalidCredentialsException){
                            message = "Kode Yang Anda Masukan Salah"
                        }
                        BottomSheets.error(
                                this.getString(R.string.ups),
                                message,
                                useTitle = true,
                                useSubTitle = true,
                                buttonMessage = this.getString(R.string.ok),
                                TAG = this.tag.toString(),
                                activity = this.activityCompat
                        )
                    }
                }
    }

    override fun onClick(v: View?) {
        if (v == this.buttonSignin){
            val code = this@OtpBottomSheetDialogFragment.inputCode.text.toString()
            if (TextUtils.isEmpty(code)){
                this@OtpBottomSheetDialogFragment.inputCode.error = "Kode Belum Di Isi"
                return
            }
            this.verifyPhoneNumberWithCode(this.storedVerificationID, code)
        }
    }

    private fun verifyPhoneNumberWithCode(storedVerificationID: String, code : String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationID, code)

        this.signinWithCredential(credential)
    }
}
