package com.steadytech.impian.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.createBalloon
import com.steadytech.impian.R
import com.steadytech.impian.adapter.WishlistAdapter
import com.steadytech.impian.bottomsheet.CreateGoalsBottomSheetFragment
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class MainActivity : AppCompatActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    private lateinit var textCurrentSavingAmount: TextView
    private lateinit var textTitleCurrentSavingAmount: TextView
    private lateinit var textNotHaveGoals: TextView
    private lateinit var textYourGoals: TextView

    private lateinit var linearNotHaveGoals : LinearLayout

    private lateinit var buttonAddGoals : MaterialButton

    private lateinit var materialTab: TabLayout

    private lateinit var imagePortfolio: ImageView

    private lateinit var fabAdd: FloatingActionButton

    private lateinit var recyclerTodo: RecyclerView

    private lateinit var realm: Realm

    private lateinit var wishlists: RealmResults<Wishlist>
    private lateinit var savings: RealmResults<Saving>

    var amount = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        this.supportActionBar!!.hide()

        this.init()

        this.data()

        this.fabAdd.setOnClickListener(this)
        this.imagePortfolio.setOnClickListener(this)
        this.buttonAddGoals.setOnClickListener(this)

        this.materialTab.addOnTabSelectedListener(this)
    }

    private fun data() {
        for (i in savings) {
            this.amount += i.amount
        }

        val spannable = SpannableString(GeneralHelper.currencyFormat(this.amount))
        spannable.setSpan(RelativeSizeSpan(0.8f), 0, 2, 0)
        this.textCurrentSavingAmount.text = spannable
    }

    @SuppressLint("Range")
    private fun init() {
        this.realm = Realm.getDefaultInstance()
        this.wishlists = this.realm.where<Wishlist>().findAll()
        this.savings = this.realm.where<Saving>().findAll()



        this.fabAdd = findViewById(R.id.fabAdd)
        this.materialTab = findViewById(R.id.tabLayout)
        this.linearNotHaveGoals = findViewById(R.id.linearNotHaveGoals)

        this.buttonAddGoals = findViewById(R.id.buttonAddGoals)
        this.buttonAddGoals.typeface = FontsHelper.INTER.regular(this)

        this.textNotHaveGoals = findViewById(R.id.textNotHaveGoals)
        this.textNotHaveGoals.typeface = FontsHelper.INTER.regular(this)

        this.textCurrentSavingAmount = findViewById(R.id.textCurrentAmount)
        this.textCurrentSavingAmount.typeface = FontsHelper.INTER.bold(this)

        this.textYourGoals = findViewById(R.id.textYourGoals)
        this.textYourGoals.typeface = FontsHelper.INTER.medium(this)

        this.textTitleCurrentSavingAmount = findViewById(R.id.textTitleCurrentSavingAmount)
        this.textTitleCurrentSavingAmount.typeface = FontsHelper.INTER.regular(this)

        this.recyclerTodo = findViewById(R.id.recyclerTodo)
        this.recyclerTodo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        this.recyclerTodo.adapter = WishlistAdapter(wishlists, this)

        this.imagePortfolio = findViewById(R.id.imagePortfolio)
        createBalloon(this){
            setText("Ringkasan Impian Kamu")
            autoDismissDuration = 3000
            backgroundColor = this@MainActivity.resources.getColor(R.color.yellow_600)
            textColor = this@MainActivity.resources.getColor(R.color.material_black)
            paddingBottom = 8
            paddingLeft = 8
            paddingTop = 8
            paddingRight = 8
            arrowOrientation = ArrowOrientation.TOP
            arrowPosition = 0.6F
            marginRight = 8
        }.showAlignBottom(this.imagePortfolio)

        if (this.wishlists.isEmpty()){
            this.empty()
        }else{
            this.notEmpty()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            this.fabAdd -> {
                val createGoalsBottomSheetFragment = CreateGoalsBottomSheetFragment()
                createGoalsBottomSheetFragment.show(this.supportFragmentManager, "MainActivity")
            }
            this.imagePortfolio -> {
                startActivity(Intent(this, PortfolioActivity::class.java).putExtra("currentBalance", this.amount))
            }
            this.buttonAddGoals -> {
                startActivity(Intent(this, CreateGoalsActivity::class.java))
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab!!.position == 0) {
            this.wishlists = this.realm.where<Wishlist>().findAll()
            this.recyclerTodo.adapter = WishlistAdapter(this.wishlists, this)
            if (this.wishlists.isEmpty()){
                this.empty()
            }else{
                this.notEmpty()
            }
        } else {
            this.wishlists = this.realm.where<Wishlist>().equalTo("isCompleted", true).findAll()
            this.recyclerTodo.adapter = WishlistAdapter(this.wishlists, this)
            if (this.wishlists.isEmpty()){
                this.linearNotHaveGoals.visibility = View.VISIBLE
                this.fabAdd.visibility = View.GONE
                this.buttonAddGoals.visibility = View.GONE
                this.textNotHaveGoals.text = this.getString(R.string.your_not_have_completed_goals)
            }else{
                this.notEmpty()
            }
        }
    }

    private fun empty(){
        this.linearNotHaveGoals.visibility = View.VISIBLE
        this.recyclerTodo.visibility = View.GONE
        this.buttonAddGoals.visibility = View.VISIBLE
        this.fabAdd.visibility = View.GONE
        this.textNotHaveGoals.text = this.getString(R.string.goals_not_found)
    }

    private fun notEmpty(){
        this.recyclerTodo.visibility = View.VISIBLE
        this.linearNotHaveGoals.visibility = View.GONE
        this.fabAdd.visibility = View.VISIBLE
    }
}