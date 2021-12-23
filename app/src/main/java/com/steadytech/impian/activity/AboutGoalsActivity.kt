package com.steadytech.impian.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steadytech.impian.R
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoWishlist
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.DatabaseHelper
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.kotlin.where

class AboutGoalsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var fabEdit: FloatingActionButton
    private lateinit var fabCancel: FloatingActionButton
    private lateinit var fabSave: FloatingActionButton
    private lateinit var fabDelete: FloatingActionButton

    private lateinit var imageBack: ImageView

    private lateinit var inputGoals: EditText
    private lateinit var inputAmount: EditText
    private lateinit var inputDescribe: EditText

    private lateinit var database: AppDatabase
    private lateinit var daoWishlist: DaoWishlist

    private lateinit var wishList: EntityWishlist

    private var id: Long = 0L
    private var mode: String = Constant.MODE.VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_about_goals)

        this.supportActionBar!!.hide()

        this.extract()

        this.init()

        this.validate()

        this.data()

        this.fabEdit.setOnClickListener(this)
        this.fabSave.setOnClickListener(this)
        this.fabCancel.setOnClickListener(this)
        this.fabDelete.setOnClickListener(this)
        this.imageBack.setOnClickListener(this)
    }

    private fun data() {
        this.inputGoals.setText(this.wishList.name)
        this.inputAmount.setText(this.wishList.amount.toString())
        this.inputDescribe.setText(this.wishList.description)
    }

    private fun extract() {
        this.id = this.intent.getLongExtra("id", 0L)
    }


    private fun init() {
        this.database = DatabaseHelper.localDb(this)
        this.daoWishlist = this.database.daoWishlist()
        this.wishList = this.daoWishlist.get(this.id)

        this.fabEdit = findViewById(R.id.fabEdit)
        this.fabSave = findViewById(R.id.fabSave)
        this.fabCancel = findViewById(R.id.fabCancel)
        this.fabDelete = findViewById(R.id.fabDelete)

        this.inputGoals = findViewById(R.id.inputGoals)
        this.inputAmount = findViewById(R.id.inputAmount)
        this.inputDescribe = findViewById(R.id.inputDescribe)

        FontsHelper.INTER.regular(this, this.inputDescribe, this.inputAmount, this.inputGoals)

        this.imageBack = findViewById(R.id.imageBack)
    }

    private fun validate() {
        if (wishList.isCompleted == true) {
            if (this.mode == Constant.MODE.VIEW) {
                this.fabSave.visibility = View.GONE
                this.fabEdit.visibility = View.VISIBLE
                this.fabCancel.visibility = View.GONE
                this.fabDelete.visibility = View.VISIBLE

                this.inputGoals.isEnabled = false
                this.inputAmount.isEnabled = false
                this.inputDescribe.isEnabled = false
            } else {
                this.fabSave.visibility = View.VISIBLE
                this.fabCancel.visibility = View.VISIBLE
                this.fabEdit.visibility = View.GONE
                this.fabDelete.visibility = View.GONE

                this.inputGoals.isEnabled = true
                this.inputAmount.isEnabled = true
                this.inputDescribe.isEnabled = true
            }
        } else {
            this.fabSave.visibility = View.GONE
            this.fabEdit.visibility = View.GONE
            this.fabCancel.visibility = View.GONE
            this.fabDelete.visibility = View.VISIBLE

            this.inputGoals.isEnabled = false
            this.inputAmount.isEnabled = false
            this.inputDescribe.isEnabled = false
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            this.fabEdit -> {
                this.editMode()
            }
            this.fabSave -> {
                this.save()
            }
            this.imageBack -> {
                super.onBackPressed()
            }
            this.fabCancel -> {
                this.cancel()
            }
            this.fabDelete -> {
                this.delete()
            }
        }
    }

    private fun delete() {
        BottomSheets.confirmation(
                this.getString(R.string.delete_your_goals),
                this.getString(R.string.subtitle_delete_your_goals),
                this.getString(R.string.yes_continue),
                Constant.TAG.DetailGoalsActivity,
                this,
                View.OnClickListener {
                    this.daoWishlist.delete(this.wishList)
                    startActivity(Intent(this@AboutGoalsActivity, MainActivity::class.java))
                    finish()
                }
        )
    }

    private fun cancel() {
        this.mode = Constant.MODE.VIEW
        this.validate()
    }


    private fun editMode() {
        this.mode = Constant.MODE.EDIT
        this.validate()
    }


    private fun save() {
        if (this.inputGoals.text.isNotEmpty() && this.inputAmount.text.isNotEmpty() && this.inputAmount.text.toString().replace(".", "") != "0") {
            BottomSheets.confirmation(
                    this.getString(R.string.update_your_goals),
                    this.getString(R.string.subtile_update_your_goals),
                    this.getString(R.string.yes_continue),
                    Constant.TAG.AboutGoalsActivity,
                    this,
                    View.OnClickListener {
                        this.mode = Constant.MODE.VIEW

                        this.wishList.name = this.inputGoals.text.toString()
                        this.wishList.amount = this.inputAmount.text.toString().replace(".", "").toLong()
                        this.wishList.description = this.inputDescribe.text.toString()

                        this.daoWishlist.update(this.wishList)
                        startActivity(Intent(this, DetailGoalsActivity::class.java).putExtra("id", this.id))
                        finish()
                    }
            )
        } else {
            BottomSheets.error(
                    this.getString(R.string.please_fill_data),
                    this.getString(R.string.description_please_fill_data),
                    useTitle = true,
                    useSubTitle = true,
                    buttonMessage = this.getString(R.string.ok),
                    TAG = Constant.TAG.AboutGoalsActivity,
                    activity = this
            )
        }
    }
}