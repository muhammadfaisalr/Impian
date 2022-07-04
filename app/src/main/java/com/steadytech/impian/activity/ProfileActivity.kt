package com.steadytech.impian.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.steadytech.impian.R
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.databinding.ActivityProfileBinding
import com.steadytech.impian.helper.*
import com.steadytech.impian.model.firebase.User
import java.util.*
import kotlin.collections.ArrayList


class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textName : TextView
    private lateinit var textFullname : TextView
    private lateinit var textMotto : TextView
    private lateinit var textLogout : TextView
    private lateinit var textPortfolio : TextView
    private lateinit var textDevice : TextView
    private lateinit var textEdit : TextView
    private lateinit var textBackup : TextView

    private lateinit var linearLogout : LinearLayout
    private lateinit var linearPortfolio : LinearLayout
    private lateinit var linearDevice : LinearLayout
    private lateinit var linearEdit : LinearLayout
    private lateinit var linearBackup : LinearLayout

    private lateinit var reference: DatabaseReference

    private lateinit var savings: ArrayList<EntitySaving>
    private lateinit var wishlists: ArrayList<EntityWishlist>

    private lateinit var localDb : AppDatabase
    private lateinit var binding : ActivityProfileBinding

    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneralHelper.changeStatusBarColor(this, R.color.material_black)
        this.binding = ActivityProfileBinding.inflate(this.layoutInflater)
        super.setContentView(this.binding.root)
        this.supportActionBar!!.hide()

        this.init()

        this.data()


    }

    private fun init() {
        this.userId = UserHelper.getUid()!!
        this.localDb = DatabaseHelper.localDb(this)
        this.reference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_REFERENCE.USER)

        this.savings = this.localDb.daoSaving().getAll() as ArrayList<EntitySaving>
        this.wishlists = this.localDb.daoWishlist().getByStatus(true) as ArrayList<EntityWishlist>


        this.textName = this.binding.textName
        this.textFullname = this.binding.textFullname
        this.textMotto = this.binding.textMotto
        this.textLogout = this.binding.textLogout
        this.textBackup = this.binding.textBackup
        this.textEdit = this.binding.textEdit
        this.textPortfolio = this.binding.textPortfolio
        this.textDevice = this.binding.textDevice

        this.linearLogout = this.binding.linearLogout
        this.linearBackup = this.binding.linearBackup
        this.linearDevice = this.binding.linearDevice
        this.linearEdit = this.binding.linearEdit
        this.linearPortfolio = this.binding.linearPortfolio

        FontsHelper.INTER.regular(this, this.textPortfolio, this.textEdit, this.textDevice, this.textBackup)
        FontsHelper.INTER.medium(this, this.textName, this.textFullname, this.textLogout)
        FontsHelper.INTER.light(this, this.textMotto)

        this.linearLogout.setOnClickListener(this)
        this.linearPortfolio.setOnClickListener(this)

        GeneralHelper.makeClickable(this, this.binding.linearCalculator)
    }


    private fun data() {
        var amount = 0L
        for (i in savings) {
            amount += i.amount!!
        }

        this.reference.child(this.userId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(ProfileActivity::class.java.simpleName, error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(User::class.java)!!
                this@ProfileActivity.textName.text = GeneralHelper.textAvatar(DatabaseHelper.decryptString(data.name))
                this@ProfileActivity.textFullname.text = DatabaseHelper.decryptString(data.name)
                this@ProfileActivity.textMotto.text = DatabaseHelper.decryptString(data.motto)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            this.linearLogout -> {
                this.logout()
            }
            this.linearPortfolio -> {
                this.portfolio()
            }
            this.binding.linearCalculator -> {
                this.calculator()
            }
        }
    }

    private fun calculator() {
        startActivity(Intent(this, CalculatorActivity::class.java))
    }

    private fun portfolio() {
        startActivity(Intent(this, PortfolioActivity::class.java))
    }

    private fun logout() {
        BottomSheets.confirmation(
            this.getString(R.string.logout),
            this.getString(R.string.subtitle_logout),
            this.getString(R.string.yes_continue),
            ProfileActivity::class.java.simpleName,
            this@ProfileActivity,
            View.OnClickListener {
                FirebaseHelper.auth().signOut()
                startActivity(
                    Intent(
                        this,
                        SigninActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                this.finish()
            }
        )
    }

    private fun share() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Allow the Storage Permission", Toast.LENGTH_LONG).show()
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 401)
//        } else {
////            this.cardDetail.isDrawingCacheEnabled = true
////            this.cardDetail.setCardBackgroundColor(this.resources.getColor(android.R.color.darker_gray))
//            val b: Bitmap = this.cardDetail.getDrawingCache()
//            val random = Random().nextInt(100000 - 100 + 1) + 100000
//            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//            val location = "/IMG-SHARE-$random-IMPIAN.png"
//            val path = root.toString() + location
//            val imageDir = File(root, location)
//            try {
//                b.compress(Bitmap.CompressFormat.PNG, 95, FileOutputStream(path))
//                Log.d(ProfileActivity::class.java.simpleName, "Success Convert Card to Image Type")
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//                Log.d(ProfileActivity::class.java.simpleName, "Fail Convert Card to Image Type")
//            }
//            val finalPath = FileInputStream(File(path))
//            finalPath.close()
//            val i = Intent()
//            i.action = Intent.ACTION_SEND
//            i.type = "image/png"
//            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageDir.absolutePath))
//            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            startActivity(Intent.createChooser(i, "Bagikan Portolio Kamu"))
//            this.cardDetail.setCardBackgroundColor(this.resources.getColor(R.color.white))
//        }
    }
}