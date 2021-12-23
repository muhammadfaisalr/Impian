package com.steadytech.impian.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steadytech.impian.R
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.database.dao.DaoWishlist
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.helper.*
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddBalanceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var textRecommendation: TextView
    private lateinit var textId: TextView

    private lateinit var inputGoals: EditText
    private lateinit var inputAmount: EditText
    private lateinit var inputDescription: EditText

    private lateinit var fabSave: FloatingActionButton

    private lateinit var wishlists: List<EntityWishlist>
    private lateinit var strings: ArrayList<String>
    private lateinit var ids: ArrayList<String>

    private lateinit var database: AppDatabase
    private lateinit var daoSaving: DaoSaving
    private lateinit var daoWishlist: DaoWishlist

    private lateinit var goals: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_add_balance)
        this.supportActionBar!!.hide()

        this.extract()

        this.init()

        this.inputGoals.setOnClickListener(this)
        this.fabSave.setOnClickListener(this)
    }

    private fun extract() {
        this.goals = this.intent.getStringExtra("goals").toString()
    }

    private fun init() {
        this.database = DatabaseHelper.localDb(this)
        this.daoSaving = this.database.daoSaving()
        this.daoWishlist = this.database.daoWishlist()

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textSubtitle = findViewById(R.id.textSubtitle)
        this.textRecommendation = findViewById(R.id.textRecomendation)
        this.inputGoals = findViewById(R.id.inputGoals)
        this.inputAmount = findViewById(R.id.inputAmount)
        this.textId = findViewById(R.id.textId)
        this.inputDescription = findViewById(R.id.inputDescription)

        FontsHelper.INTER.regular(
            this,
            this.textSubtitle,
            this.textRecommendation,
            this.inputGoals,
            this.inputAmount,
            this.textId,
            this.inputDescription
        )

        this.fabSave = findViewById(R.id.fabSave)
    }

    override fun onClick(v: View?) {
        if (v == this.inputGoals) {
            this.showSpinner()
        } else if (v == this.fabSave) {
            this.save()
        }
    }

    private fun save() {
        if (this.inputGoals.text.isNotEmpty() && this.inputAmount.text.isNotEmpty()) {
            if (this.inputDescription.text.isEmpty()) {
                this.inputDescription.setText("")
            }
            this.daoSaving.insert(
                EntitySaving(
                    null,
                    this.inputGoals.text.toString(),
                    this.textId.text.toString().toLong(),
                    this.inputAmount.text.toString().replace(".", "").toLong(),
                    this.inputDescription.text.toString(),
                    LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE),
                    Conditions.NO
                )
            )
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showSpinner() {
        this.strings = ArrayList()
        this.ids = ArrayList()

        if (this.strings.isNotEmpty()) {
            this.strings.clear()
        }

        if (this.ids.isNotEmpty()) {
            this.ids.clear()
        }

        if (this.goals == "null") {
            this.wishlists = this.daoWishlist.getUnComplete()
        } else {
            this.wishlists = this.daoWishlist.getUnCompleteByName(this.goals)
        }

        for (wishlist in this.wishlists) {
            this.strings.add(wishlist.name!!)
            this.ids.add(wishlist.id.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.strings)

        AlertDialog.Builder(this)
            .setAdapter(adapter) { dialog, which ->
                this.inputGoals.setText(this.strings[which])
                this.textId.text = this.ids[which]
                this.textRecommendation.text = GeneralHelper.calculateRecommendationSaving(
                    this.strings[which],
                    this.daoWishlist
                ) + " /Hari"
            }
            .create().show()
    }
}