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
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddBalanceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textSubtitle : TextView
    private lateinit var textRecommendation : TextView
    private lateinit var textId : TextView

    private lateinit var inputGoals : EditText
    private lateinit var inputAmount : EditText
    private lateinit var inputDescription : EditText

    private lateinit var fabSave : FloatingActionButton

    private lateinit var realm : Realm;
    private lateinit var wishlists : RealmResults<Wishlist>
    private lateinit var strings : ArrayList<String>
    private lateinit var ids : ArrayList<String>

    private lateinit var goals : String

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
        this.realm = Realm.getDefaultInstance()

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textSubtitle = findViewById(R.id.textSubtitle)
        this.textSubtitle.typeface = FontsHelper.INTER.regular(this)

        this.textRecommendation = findViewById(R.id.textRecomendation)
        this.textRecommendation.typeface = FontsHelper.INTER.regular(this)

        this.inputGoals = findViewById(R.id.inputGoals)
        this.inputGoals.typeface = FontsHelper.INTER.regular(this)

        this.inputAmount = findViewById(R.id.inputAmount)
        this.inputAmount.typeface = FontsHelper.INTER.regular(this)

        this.textId = findViewById(R.id.textId)

        this.inputDescription = findViewById(R.id.inputDescription)
        this.inputDescription.typeface = FontsHelper.INTER.regular(this)

        this.fabSave = findViewById(R.id.fabSave)
    }

    override fun onClick(v: View?) {
        if (v == this.inputGoals){
            this.showSpinner()
        }else if (v == this.fabSave) {
            this.save()
        }
    }

    private fun save() {
        if (this.inputGoals.text.isNotEmpty() && this.inputAmount.text.isNotEmpty()){
            if (this.inputDescription.text.isEmpty()){
                this.inputDescription.setText("")
            }
            this.realm.executeTransaction{it ->
                val saving = it.createObject<Saving>(System.nanoTime())
                saving.name = this.inputGoals.text.toString()
                saving.amount = this.inputAmount.text.toString().replace(".", "").toLong()
                saving.description = this.inputDescription.text.toString()
                saving.targetId = this.textId.text.toString().toLong()
                saving.savingDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

                it.copyToRealmOrUpdate(saving)
            }
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showSpinner() {
        this.strings = ArrayList()
        this.ids = ArrayList()

        if (this.strings.isNotEmpty()){
            this.strings.clear()
        }

        if (this.ids.isNotEmpty()){
            this.ids.clear()
        }

        if (this.goals == "null"){
            this.wishlists = this.realm.where<Wishlist>().equalTo("isCompleted", false).findAll()
        }else{
            this.wishlists = this.realm.where<Wishlist>().equalTo("isCompleted", false).equalTo("name", this.goals).findAll()
        }

        for (wishlist in this.wishlists){
            this.strings.add(wishlist.name)
            this.ids.add(wishlist.id.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.strings)

        AlertDialog.Builder(this)
                .setAdapter(adapter) { dialog, which ->
                    this.inputGoals.setText(this.strings[which])
                    this.textId.text = this.ids[which]
                    this.textRecommendation.text = GeneralHelper.calculateRecommendationSaving(this.strings[which], this.realm) + " /Hari"
                }
                .create().show()
    }
}