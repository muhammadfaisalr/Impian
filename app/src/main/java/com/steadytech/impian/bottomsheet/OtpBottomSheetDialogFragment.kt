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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.philliphsu.bottomsheetpickers.date.TextViewWithCircularIndicator
import com.poovam.pinedittextfield.SquarePinField
import com.steadytech.impian.R
import com.steadytech.impian.activity.CreateGoalsActivity
import com.steadytech.impian.activity.MainActivity
import com.steadytech.impian.activity.SignupActivity
import com.steadytech.impian.custom.Loading
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.DatabaseHelper
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.firebase.User
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OtpBottomSheetDialogFragment(private val phoneNumber : String, private val mode : String, private val name : String) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textResend : TextView

    private lateinit var buttonSignin : MaterialButton

    private lateinit var inputCode : SquarePinField

    private lateinit var activityCompat: AppCompatActivity

    private lateinit var auth : FirebaseAuth

    private lateinit var storedVerificationID: String

    private lateinit var databaseReference : DatabaseReference

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
        this.textTitle.text = this.getString(R.string.otp_sended) + " " + phoneNumber

        this.textResend = view.findViewById(R.id.textResend)
        this.textResend.typeface = FontsHelper.INTER.regular(this.activityCompat)

        this.buttonSignin = view.findViewById(R.id.buttonSignin)
        this.buttonSignin.typeface = FontsHelper.INTER.regular(this.activityCompat)

        this.inputCode = view.findViewById(R.id.inputCode)
        this.inputCode.typeface = FontsHelper.INTER.medium(this.activityCompat)
    }


    private fun sendOtp() {
        this.buttonSignin.visibility = View.GONE
        this.auth.setLanguageCode("id")

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("VerificationComplete", "onVerificationComplete: $p0")
                this@OtpBottomSheetDialogFragment.signinWithCredential(p0)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                var message = this@OtpBottomSheetDialogFragment.getString(R.string.something_wrong)

                Log.d("VerificationFail", "onVerificationFail: $e")

                if (e is FirebaseAuthInvalidCredentialsException){
                    Log.d("VerificationFail", "onVerificationFail: $e")
                    message = "Tidak Dapat Mengirim OTP Karena Kesalahan Kredensial"
                }else if (e is FirebaseTooManyRequestsException){
                    Log.d("VerificationFail", "onVerificationFail: $e")
                    message = "Terlalu Banyak Permintaan Kode OTP. Anda Dapat Memintanya Kembali Dalam 5 Menit."
                }

                this@OtpBottomSheetDialogFragment.dismiss()

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
                this@OtpBottomSheetDialogFragment.buttonSignin.visibility = View.VISIBLE
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
        val loading = Loading(this.activityCompat)
        loading.setCancelable(false)
        this.auth.signInWithCredential(credential)
                .addOnCompleteListener{ task->
                    loading.show()
                    if (task.isSuccessful){
                        this.databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_REFERENCE.USER).child(task.result!!.user!!.uid)

                        if(mode == Constant.MODE.SIGN_IN){
                            // Jika Dalam Mode Login, Pengguna Di Arahkan Ke Halaman Main.
                            this.databaseReference.addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(error: DatabaseError) {
                                    Log.d(this@OtpBottomSheetDialogFragment.tag, "Database Error ${error.message}")
                                    loading.dismiss()
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (mode == Constant.MODE.SIGN_IN) {
                                        val user = snapshot.getValue(User::class.java)

                                        if (user == null) {
                                            loading.dismiss()
                                            BottomSheets.confirmation(
                                                this@OtpBottomSheetDialogFragment.getString(R.string.account_not_found),
                                                this@OtpBottomSheetDialogFragment.getString(R.string.account_not_fount_description),
                                                this@OtpBottomSheetDialogFragment.getString(R.string.yes_register),
                                                this@OtpBottomSheetDialogFragment.tag.toString(),
                                                activityCompat,
                                                View.OnClickListener {
                                                    startActivity(
                                                        Intent(
                                                            this@OtpBottomSheetDialogFragment.activityCompat,
                                                            SignupActivity::class.java
                                                        )
                                                    )
                                                }
                                            )
                                            return
                                        }

                                        Log.d(
                                            this@OtpBottomSheetDialogFragment.tag.toString(),
                                            "Sign In With Credential Success"
                                        )

                                        loading.dismiss()
                                        startActivity(
                                            Intent(
                                                this@OtpBottomSheetDialogFragment.activityCompat,
                                                MainActivity::class.java
                                            )
                                        )
                                        this@OtpBottomSheetDialogFragment.activityCompat.finish()
                                    }
                                }

                            })
                        }else{
                            val user = User(
                                    DatabaseHelper.encryptString(task.result!!.user!!.uid),
                                    DatabaseHelper.encryptString(this.name),
                                    DatabaseHelper.encryptString(this.getString(R.string.motto_not_found)),
                                    DatabaseHelper.encryptString(this.phoneNumber)
                            )

                            this.databaseReference.setValue(user)
                                    .addOnSuccessListener {
                                        loading.dismiss()
                                        startActivity(Intent(this.activityCompat, CreateGoalsActivity::class.java))
                                        this.activityCompat.finish()
                                    }
                                    .addOnFailureListener{
                                        loading.dismiss()
                                        BottomSheets.error(
                                                title = this.getString(R.string.ups),
                                                subtitle = it.message.toString(),
                                                useTitle = true,
                                                useSubTitle = true,
                                                buttonMessage = this.getString(R.string.ok),
                                                TAG = SignupActivity::class.java.simpleName,
                                                activity = this.activityCompat
                                        )
                                    }
                        }
                    }else{
                        loading.dismiss()
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
            .addOnFailureListener {
                Log.d(this@OtpBottomSheetDialogFragment.tag, it.message.toString())
                Toast.makeText(this@OtpBottomSheetDialogFragment.activityCompat, "Gagal Login Karena ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onClick(v: View?) {
        if (v == this.buttonSignin){
            val code = this@OtpBottomSheetDialogFragment.inputCode.text.toString()
            if (TextUtils.isEmpty(code)){
                this@OtpBottomSheetDialogFragment.inputCode.error = "Anda Belum Memasukkan Kode"
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
