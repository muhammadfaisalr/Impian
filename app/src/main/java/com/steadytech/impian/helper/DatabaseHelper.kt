package com.steadytech.impian.helper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.steadytech.impian.database.AppDatabase
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DatabaseHelper {
    companion object{
        private val ALGORITHM = "Blowfish"
        private val MODE = "Blowfish/CBC/PKCS5Padding"
        private val IV = "abcdefgh"
        private val KEY = "Cards"

        fun localDb(context: Context) : AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, Constant.NAME.LOCAL_DB).allowMainThreadQueries().build()
        }

        fun encryptString(value: String?): String {
            if (value == null || value.isEmpty()) return ""
            val secretKeySpec =
                    SecretKeySpec(KEY.toByteArray(), ALGORITHM)
            var cipher: Cipher? = null
            try {
                cipher = Cipher.getInstance(MODE)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }
            try {
                cipher!!.init(
                        Cipher.ENCRYPT_MODE,
                        secretKeySpec,
                        IvParameterSpec(IV.toByteArray())
                )
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            }
            var values: ByteArray? = ByteArray(0)
            try {
                values = cipher!!.doFinal(value.toByteArray())
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            }
            return Base64.encodeToString(values, Base64.DEFAULT)
        }

        fun decryptString(value: String?): String {
            if (value == null || value.isEmpty()) return ""
            var values: ByteArray? = ByteArray(0)
            try {
                values = Base64.decode(value, Base64.DEFAULT)
            } catch (e: Exception) {
                values = value.toByteArray()
                e.printStackTrace()
            }
            val secretKeySpec =
                    SecretKeySpec(KEY.toByteArray(), ALGORITHM)
            var cipher: Cipher? = null
            try {
                cipher = Cipher.getInstance(MODE)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }
            try {
                cipher!!.init(
                        Cipher.DECRYPT_MODE,
                        secretKeySpec,
                        IvParameterSpec(IV.toByteArray())
                )
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            }
            try {
                return String(cipher!!.doFinal(values))
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            }
            return ""
        }
    }
    class FIREBASE {
        companion object{
            fun getImpianPath(context: Context) : DatabaseReference{
                val auth = FirebaseAuth.getInstance().currentUser
                val database = FirebaseDatabase.getInstance().getReference(Constant.PATH.IMPIAN)

                Log.d(DatabaseHelper::class.java.simpleName, "Start get Impian Path with Current User ${auth!!.uid}")

                return database.child(auth.uid)
            }
        }
    }
}