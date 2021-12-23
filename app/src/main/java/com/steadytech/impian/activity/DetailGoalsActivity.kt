package com.steadytech.impian.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.steadytech.impian.R
import com.steadytech.impian.adapter.SavingAdapter
import com.steadytech.impian.bottomsheet.MenuBottomSheetDialogFragment
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.database.dao.DaoWishlist
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.helper.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*


class DetailGoalsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var behavior: LinearLayout
    private lateinit var relativeParent: RelativeLayout
    private lateinit var linearEmptyBalance: LinearLayout

    private lateinit var imageDove: ImageView

    private lateinit var textTitle: TextView
    private lateinit var textTimeLeft: TextView
    private lateinit var textDownload: TextView
    private lateinit var textAddSaving: TextView
    private lateinit var textTitleHistory: TextView
    private lateinit var textTips: TextView
    private lateinit var textCurrentBalance: TextView
    private lateinit var textTitleCurrentBalance: TextView
    private lateinit var textTarget: TextView
    private lateinit var textCurrentProgress: TextView
    private lateinit var textTitleTarget: TextView
    private lateinit var textMenu: TextView
    private lateinit var textEmptyBalance: TextView


    private lateinit var buttonAddBalance: MaterialButton

    private lateinit var imageDivider: ImageView
    private lateinit var buttonBack: ImageView

    private lateinit var linearMarkAsAchieved: LinearLayout
    private lateinit var linearAboutGoals: LinearLayout

    private lateinit var recyclerSaving: RecyclerView

    private lateinit var fabAddBalance: ExtendedFloatingActionButton

    private lateinit var progressBar: ProgressBar

    private lateinit var datas: List<EntitySaving>
    private lateinit var wishlist: EntityWishlist

    private lateinit var database: AppDatabase
    private lateinit var daoWishlist: DaoWishlist
    private lateinit var daoSaving: DaoSaving

    private var id: Long = 0L
    private var amount: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_detail_goals)

        this.supportActionBar!!.hide()

        this.arguments()

        this.init()

        this.settingBottomSheet()

        this.data()

        this.linearMarkAsAchieved.setOnClickListener(this)
        this.linearAboutGoals.setOnClickListener(this)
        this.buttonBack.setOnClickListener(this)
        this.buttonAddBalance.setOnClickListener(this)
        this.fabAddBalance.setOnClickListener(this)
        this.textMenu.setOnClickListener(this)
    }

    private fun arguments() {
        this.id = this.intent.getLongExtra("id", 0)
    }

    private fun init() {
        this.database = DatabaseHelper.localDb(this)
        this.daoSaving = this.database.daoSaving()
        this.daoWishlist = this.database.daoWishlist()

        this.behavior = findViewById(R.id.behavior)
        this.imageDivider = findViewById(R.id.imageDivider)

        this.textMenu = findViewById(R.id.textMenu)
        this.textMenu.typeface = FontsHelper.INTER.medium(this)

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textTimeLeft = findViewById(R.id.textTimeLeft)
        this.textTimeLeft.typeface = FontsHelper.INTER.light(this)

        this.textTitleHistory = findViewById(R.id.textTitleHistory)
        this.textTitleHistory.typeface = FontsHelper.INTER.medium(this)

        this.textCurrentProgress = findViewById(R.id.textCurrentProgress)
        this.textCurrentProgress.typeface = FontsHelper.INTER.medium(this)

        this.textCurrentBalance = findViewById(R.id.textCurrentBalance)
        this.textCurrentBalance.typeface = FontsHelper.INTER.medium(this)

        this.textTarget = findViewById(R.id.textTarget)
        this.textTarget.typeface = FontsHelper.INTER.medium(this)

        this.relativeParent = findViewById(R.id.relativeParent)

        this.textDownload = findViewById(R.id.textInfo)
        this.textAddSaving = findViewById(R.id.textAchieved)
        this.textEmptyBalance = findViewById(R.id.textBalanceEmpty)
        this.textTips = findViewById(R.id.textTips)
        this.textTitleCurrentBalance = findViewById(R.id.textTitleCurrentBalance)
        this.textTitleTarget = findViewById(R.id.textTitleTarget)
        this.fabAddBalance = findViewById(R.id.fabAddBalance)
        this.buttonBack = findViewById(R.id.buttonBack)
        this.buttonAddBalance = findViewById(R.id.buttonAddBalance)

        this.linearEmptyBalance = findViewById(R.id.linearBalanceEmpty)
        this.linearMarkAsAchieved = findViewById(R.id.linearAchieved)
        this.linearAboutGoals = findViewById(R.id.linearAboutGoals)
        this.progressBar = findViewById(R.id.progressBar)
        this.recyclerSaving = findViewById(R.id.recyclerSaving)
        this.imageDove = findViewById(R.id.imageDove)

        FontsHelper.INTER.regular(
                this,
                this.textDownload,
                this.textAddSaving,
                this.textEmptyBalance,
                this.textTips,
                this.textTitleCurrentBalance,
                this.textTitleTarget,
                this.textAddSaving,
                this.buttonBack,
                this.buttonAddBalance
        )
    }

    private fun data() {

        this.wishlist = this.daoWishlist.get(this.id)
        val saving = this.daoSaving.getByWishlistID(this.id)


        if (saving == null) {
            this.linearEmptyBalance.visibility = View.VISIBLE
            this.fabAddBalance.visibility = View.GONE
        }

        if (this.wishlist.isCompleted!!) {
            this.linearMarkAsAchieved.visibility = View.GONE
            this.imageDivider.visibility = View.GONE
            this.fabAddBalance.visibility = View.GONE
        }

        val sdfEnd = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val prettyTime = PrettyTime(Locale("id", "id"))

        this.datas = this.daoSaving.getAllByWishlistID(this.id)
        this.recyclerSaving.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        this.recyclerSaving.adapter = SavingAdapter(this.datas, this)

        this.textTitle.text = this.wishlist.name
        this.textTimeLeft.text = prettyTime.format(sdfEnd.parse(this.wishlist.endDate))
        this.textTarget.text = GeneralHelper.currencyFormat(this.wishlist.amount!!)

        if (this.wishlist.category == this.getString(R.string.wedding)) {
            this.relativeParent.setBackgroundResource(R.color.pink_wedding)
            this.imageDove.visibility = View.VISIBLE
            this.fabAddBalance.setBackgroundColor(this.resources.getColor(R.color.pink_wedding))

            val window: Window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.red_300)
        }

        for (i in this.datas) {
            this.amount += i.amount!!
        }

        this.progressBar.setProgress(this.calculatePercentage().toInt(), true)
        this.textCurrentBalance.text = GeneralHelper.currencyFormat(this.amount)
        if (this.calculatePercentage().toInt() > 100) {
            this.textCurrentProgress.text = "100%"
        } else {
            this.textCurrentProgress.text = this.calculatePercentage().toInt().toString() + "%"
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun settingBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(this.behavior)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING
        bottomSheetBehavior.peekHeight = this.  getScreenHeight() / 2 + 400
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.isDraggable = true
    }

    override fun onClick(v: View?) {
        when (v) {
            this.linearMarkAsAchieved -> {
                this.achieved()
            }
            this.linearAboutGoals -> {
                this.aboutGoals()
            }
            this.buttonBack -> {
                super.onBackPressed()
            }
            this.buttonAddBalance -> {
                this.addBalance()
            }
            this.fabAddBalance -> {
                this.addBalance()
            }
            this.textMenu -> {
                this.showMenu()
            }
        }
    }

    private fun showMenu() {
        val bundle = Bundle()
        bundle.putLong(Constant.KEY.ID_WISHLIST, this.wishlist.id!!)

        val bottomSheet = MenuBottomSheetDialogFragment()
        bottomSheet.arguments = bundle
        bottomSheet.show(this.supportFragmentManager, DetailGoalsActivity::class.java.simpleName)
    }

    private fun addBalance() {
        startActivity(
                Intent(this, AddBalanceActivity::class.java).putExtra(
                        "goals",
                        this.wishlist.name
                )
        )
    }

    private fun aboutGoals() {
        startActivity(Intent(this, AboutGoalsActivity::class.java).putExtra("id", this.wishlist.id))
    }

    private fun achieved() {
        if (this.calculatePercentage().toInt() < 90) {
            BottomSheets.error(
                    this.getString(R.string.target_unreached),
                    this.getString(R.string.info_unreached_goals),
                    true,
                    true,
                    this.getString(R.string.ok),
                    DetailGoalsActivity::class.java.simpleName  ,
                    this
            )
        } else {
            BottomSheets.confirmation(
                    this.getString(R.string.ask_mark_as_achieved),
                    this.getString(R.string.description_ask_mark_as_achieved),
                    this.getString(R.string.yes_continue),
                    DetailGoalsActivity::class.java.simpleName,
                    this,
                    View.OnClickListener {
                        this.wishlist.isCompleted = true;
                        this.daoWishlist.update(this.wishlist)
                        startActivity(
                                Intent(
                                        this,
                                        MainActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
                    }
            )
        }
    }

    private fun calculatePercentage(): Float {
        return ((this.amount.toFloat().div(wishlist.amount.toString().toFloat())) * 100)
    }
}