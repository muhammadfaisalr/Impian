package com.steadytech.impian.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.steadytech.impian.R
import com.steadytech.impian.adapter.WishlistAdapter
import com.steadytech.impian.bottomsheet.CreateGoalsBottomSheetFragment
import com.steadytech.impian.bottomsheet.SavingListBottomSheetFragment
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.database.dao.DaoWishlist
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.helper.*

class MainActivity : AppCompatActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    private lateinit var textTitle: TextView
    private lateinit var textAmount: TextView
    private lateinit var textClickHere: TextView
    private lateinit var textSettings: TextView

    private lateinit var cardAmount: CardView

    private lateinit var textNotHaveGoals: TextView
    private lateinit var textYourGoals: TextView

    private lateinit var linearNotHaveGoals: LinearLayout
    private lateinit var linearSettings: LinearLayout
    private lateinit var linearAccount: LinearLayout

    private lateinit var buttonAddGoals: MaterialButton

    private lateinit var materialTab: TabLayout

    private lateinit var fabAdd: FloatingActionButton

    private lateinit var recyclerTodo: RecyclerView

    private lateinit var wishlists: List<EntityWishlist>
    private lateinit var savings: List<EntitySaving>

    private lateinit var database : AppDatabase

    private lateinit var daoWishlist : DaoWishlist
    private lateinit var daoSaving : DaoSaving

    var amount = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_main)

        this.supportActionBar!!.hide()

        this.init()

        this.data()

        this.fabAdd.setOnClickListener(this)
        this.buttonAddGoals.setOnClickListener(this)
        this.cardAmount.setOnClickListener(this)
        this.linearSettings.setOnClickListener(this)
        this.linearAccount.setOnClickListener(this)

        this.materialTab.addOnTabSelectedListener(this)
    }

    private fun data() {
        for (i in savings) {
            this.amount += i.amount!!
        }

        this.textAmount.text = GeneralHelper.currencyFormat(this.amount)
    }

    @SuppressLint("Range")
    private fun init() {
        this.database = DatabaseHelper.localDb(this)
        this.daoWishlist = this.database.daoWishlist()
        this.daoSaving = this.database.daoSaving()

        this.wishlists = this.daoWishlist.getAll()
        this.savings = this.daoSaving.getAll()

        this.fabAdd = findViewById(R.id.fabAdd)
        this.materialTab = findViewById(R.id.tabLayout)
        this.linearNotHaveGoals = findViewById(R.id.linearNotHaveGoals)

        this.textSettings = findViewById(R.id.textSettings)
        this.buttonAddGoals = findViewById(R.id.buttonAddGoals)
        this.textNotHaveGoals = findViewById(R.id.textNotHaveGoals)

        this.textAmount = findViewById(R.id.textAmount)
        this.textAmount.typeface = FontsHelper.INTER.bold(this)

        this.textClickHere = findViewById(R.id.textClickHere)
        this.textClickHere.typeface = FontsHelper.INTER.bold(this)

        this.textYourGoals = findViewById(R.id.textYourGoals)
        this.textYourGoals.typeface = FontsHelper.INTER.medium(this)

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.light(this)


        this.linearSettings = findViewById(R.id.linearSettings)
        this.linearAccount = findViewById(R.id.linearAccount)

        this.recyclerTodo = findViewById(R.id.recyclerTodo)
        this.recyclerTodo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        this.recyclerTodo.adapter = WishlistAdapter(wishlists, this)

        this.cardAmount = findViewById(R.id.cardAmount)

        FontsHelper.INTER.regular(
            this,
            this.textSettings,
            this.buttonAddGoals,
            this.textNotHaveGoals
        )

        if (this.wishlists.isEmpty()) {
            this.empty()
        } else {
            this.notEmpty()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            this.fabAdd -> {
                val createGoalsBottomSheetFragment = CreateGoalsBottomSheetFragment()
                createGoalsBottomSheetFragment.show(this.supportFragmentManager, "MainActivity")
            }
            this.buttonAddGoals -> {
                startActivity(Intent(this, CreateGoalsActivity::class.java))
            }
            this.cardAmount -> {
                val bottomSheet = SavingListBottomSheetFragment()
                bottomSheet.show(this.supportFragmentManager, MainActivity::class.java.simpleName)
            }
            this.linearSettings -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
            this.linearAccount -> {

                if (!UserHelper.isAnonymous(this)) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    BottomSheets.error(
                        "Anda Belum Masuk",
                        "Masuk Untuk Melanjutkan",
                        true,
                        true,
                        "Mengerti",
                        MainActivity::class.java.simpleName,
                        this
                    )
                }
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab!!.position == 0) {
            this.wishlists = this.daoWishlist.getAll()
            this.recyclerTodo.adapter = WishlistAdapter(this.wishlists, this)
            if (this.wishlists.isEmpty()) {
                this.empty()
            } else {
                this.notEmpty()
            }
        } else {
            this.wishlists = this.daoWishlist.getComplete()
            this.recyclerTodo.adapter = WishlistAdapter(this.wishlists, this)
            if (this.wishlists.isEmpty()) {
                this.linearNotHaveGoals.visibility = View.VISIBLE
                this.fabAdd.visibility = View.GONE
                this.buttonAddGoals.visibility = View.GONE
                this.textNotHaveGoals.text = this.getString(R.string.your_not_have_completed_goals)
            } else {
                this.notEmpty()
            }
        }
    }

    private fun empty() {
        this.linearNotHaveGoals.visibility = View.VISIBLE
        this.recyclerTodo.visibility = View.GONE
        this.buttonAddGoals.visibility = View.VISIBLE
        this.fabAdd.visibility = View.GONE
        this.textNotHaveGoals.text = this.getString(R.string.goals_not_found)
    }

    private fun notEmpty() {
        this.recyclerTodo.visibility = View.VISIBLE
        this.linearNotHaveGoals.visibility = View.GONE
        this.fabAdd.visibility = View.VISIBLE
    }
}