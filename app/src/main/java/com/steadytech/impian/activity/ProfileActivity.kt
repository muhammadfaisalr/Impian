package com.steadytech.impian.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.steadytech.impian.R
import com.steadytech.impian.adapter.MenuAdapter
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.UserHelper
import com.steadytech.impian.model.firebase.User
import com.steadytech.impian.model.realm.Menu
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textName : TextView
    private lateinit var textMotto : TextView
    private lateinit var textLogout : TextView

    private lateinit var linearLogout : LinearLayout

    private lateinit var recyclerView : RecyclerView

    private lateinit var auth : FirebaseAuth
    private lateinit var reference : DatabaseReference
    private lateinit var user : FirebaseUser

    private lateinit var menus : RealmResults<Menu>

    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_profile)

        this.supportActionBar!!.hide()

        this.init()

        this.menu()

        if (UserHelper.isNotAnonymous(this)){
            this.data()
        }else{
            this.textName.text = "Masuk atau Daftar"
            this.textMotto.text = "Dapatkan Kemudahan Meraih Impian!"
            this.textName.setOnClickListener {
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }

        this.linearLogout.setOnClickListener(this)
    }

    private fun menu() {
        this.menus = this.realm.where<Menu>().equalTo("mode", Constant.MODE.PROFILE).findAll()
        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        this.recyclerView.adapter = MenuAdapter(this.menus, this)
    }

    private fun data() {
        this.reference.child(user.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(ProfileActivity::class.java.simpleName, error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(User::class.java)!!

                this@ProfileActivity.textName.text = data.name
                this@ProfileActivity.textMotto.text = data.motto
            }
        })
    }

    private fun init() {
        this.realm = Realm.getDefaultInstance()

        this.textName = findViewById(R.id.textName)
        this.textName.typeface = FontsHelper.INTER.medium(this)

        this.textMotto = findViewById(R.id.textMotto)
        this.textMotto.typeface = FontsHelper.INTER.light(this)

        this.textLogout = findViewById(R.id.textLogout)
        this.textLogout.typeface = FontsHelper.INTER.light(this)

        this.linearLogout = findViewById(R.id.linearLogout)
        this.recyclerView = findViewById(R.id.recyclerMenu)

        this.auth = FirebaseAuth.getInstance()
        this.reference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_REFERENCE.USER)
        this.user = auth.currentUser!!
    }

    override fun onClick(v: View?) {
        if (v == this.linearLogout){
            BottomSheets.confirmation(
                this.getString(R.string.logout),
                this.getString(R.string.subtitle_logout),
                this.getString(R.string.yes_continue),
                ProfileActivity::class.simpleName.toString(),
                this,
                View.OnClickListener {
                    this.auth.signOut()
                    startActivity(Intent(this, SigninActivity::class.java))
                    finish()
                }
            )
        }
    }
}