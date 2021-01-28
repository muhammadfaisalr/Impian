package com.steadytech.impian.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.steadytech.impian.R
import com.steadytech.impian.adapter.SavingAdapter
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*


class DetailGoalsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var behavior : LinearLayout

    private lateinit var textTitle : TextView
    private lateinit var textTimeLeft : TextView
    private lateinit var textDownload : TextView
    private lateinit var textAddSaving : TextView
    private lateinit var textTitleHistory : TextView
    private lateinit var textTips : TextView
    private lateinit var textCurrentBalance : TextView
    private lateinit var textTitleCurrentBalance : TextView
    private lateinit var textTarget : TextView
    private lateinit var textCurrentProgress : TextView
    private lateinit var textTitleTarget : TextView
    private lateinit var textEmptyBalance: TextView

    private lateinit var linearEmptyBalance : LinearLayout

    private lateinit var buttonAddBalance : MaterialButton

    private lateinit var imageDivider : ImageView
    private lateinit var buttonBack : ImageView

    private lateinit var linearMarkAsAchieved : LinearLayout
    private lateinit var linearAboutGoals : LinearLayout

    private lateinit var recyclerSaving : RecyclerView

    private lateinit var fabAddBalance : ExtendedFloatingActionButton

    private lateinit var progressBar : ProgressBar

    private lateinit var realm : Realm

    private lateinit var datas : RealmResults<Saving>
    private lateinit var wishlist : Wishlist

    private var id : Long = 0L
    private var amount : Long = 0L
    private var saving : Saving? = null

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
    }

    private fun arguments() {
        this.id = this.intent.getLongExtra("id", 0)
    }

    private fun init() {
        this.realm = Realm.getDefaultInstance()

        this.behavior = findViewById(R.id.behavior)
        this.imageDivider = findViewById(R.id.imageDivider)

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textTimeLeft = findViewById(R.id.textTimeLeft)
        this.textTimeLeft.typeface = FontsHelper.INTER.light(this)

        this.textDownload = findViewById(R.id.textInfo)
        this.textDownload.typeface = FontsHelper.INTER.regular(this)

        this.textAddSaving = findViewById(R.id.textAchieved)
        this.textAddSaving.typeface = FontsHelper.INTER.regular(this)

        this.textTitleHistory = findViewById(R.id.textTitleHistory)
        this.textTitleHistory.typeface = FontsHelper.INTER.medium(this)

        this.textCurrentProgress = findViewById(R.id.textCurrentProgress)
        this.textCurrentProgress.typeface = FontsHelper.INTER.medium(this)

        this.textEmptyBalance = findViewById(R.id.textBalanceEmpty)
        this.textEmptyBalance.typeface = FontsHelper.INTER.regular(this)

        this.buttonAddBalance = findViewById(R.id.buttonAddBalance)
        this.buttonAddBalance.typeface = FontsHelper.INTER.regular(this)

        this.linearEmptyBalance = findViewById(R.id.linearBalanceEmpty)

        this.textTips = findViewById(R.id.textTips)
        this.textTips.typeface = FontsHelper.INTER.regular(this)

        this.textCurrentBalance = findViewById(R.id.textCurrentBalance)
        this.textCurrentBalance.typeface = FontsHelper.INTER.medium(this)

        this.textTitleCurrentBalance = findViewById(R.id.textTitleCurrentBalance)
        this.textTitleCurrentBalance.typeface = FontsHelper.INTER.regular(this)

        this.textTarget = findViewById(R.id.textTarget)
        this.textTarget.typeface = FontsHelper.INTER.medium(this)

        this.textTitleTarget = findViewById(R.id.textTitleTarget)
        this.textTitleTarget.typeface = FontsHelper.INTER.regular(this)

        this.fabAddBalance = findViewById(R.id.fabAddBalance)
        this.fabAddBalance.typeface = FontsHelper.INTER.regular(this)

        this.buttonBack = findViewById(R.id.buttonBack)

        this.linearMarkAsAchieved = findViewById(R.id.linearAchieved)

        this.linearAboutGoals = findViewById(R.id.linearAboutGoals)

        this.progressBar = findViewById(R.id.progressBar)

        this.recyclerSaving = findViewById(R.id.recyclerSaving)
    }

    private fun data(){

        this.wishlist = this.realm.where<Wishlist>().equalTo("id", this.id).findFirst()!!
        this.saving = this.realm.where<Saving>().equalTo("targetId", this.id).findFirst()

        if (this.saving == null){
            this.linearEmptyBalance.visibility = View.VISIBLE
            this.fabAddBalance.visibility = View.GONE
        }

        if (this.wishlist.isCompleted){
            this.linearMarkAsAchieved.visibility = View.GONE
            this.imageDivider.visibility = View.GONE
            this.fabAddBalance.visibility = View.GONE
        }

        val sdfEnd = SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH)
        val prettyTime = PrettyTime(Locale("id", "id"))

        this.datas = this.realm.where<Saving>().equalTo("targetId", this.id).findAll()
        this.recyclerSaving.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        this.recyclerSaving.adapter = SavingAdapter(this.datas, this)

        this.textTitle.text = this.wishlist.name
        this.textTimeLeft.text = prettyTime.format(sdfEnd.parse(this.wishlist.endDate))
        this.textTarget.text = GeneralHelper.currencyFormat(this.wishlist.amount)

        for (i in this.datas){
            this.amount += i.amount
        }

        this.progressBar.setProgress(this.calculatePercentage().toInt(), true)
        this.textCurrentBalance.text = GeneralHelper.currencyFormat(this.amount)
        if (this.calculatePercentage().toInt() > 100){
            this.textCurrentProgress.text = "100%"
        }else{
            this.textCurrentProgress.text = this.calculatePercentage().toInt().toString() + "%"
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun settingBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(this.behavior)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING
        bottomSheetBehavior.peekHeight = this.getScreenHeight() / 2 + 400
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
        }
    }

    private fun addBalance() {
        startActivity(Intent(this, AddBalanceActivity::class.java).putExtra("goals", this.wishlist.name))
    }

    private fun aboutGoals() {
        startActivity(Intent(this, AboutGoalsActivity::class.java).putExtra("id", this.id))
    }

    private fun achieved() {
        if (this.calculatePercentage().toInt() < 90){
            BottomSheets.error(
                this.getString(R.string.target_unreached),
                this.getString(R.string.info_unreached_goals),
                true,
                true,
                this.getString(R.string.ok),
                "DetailGoalsActivity",
                this
            )
        }else{
            BottomSheets.confirmation(
                    this.getString(R.string.ask_mark_as_achieved),
                    this.getString(R.string.description_ask_mark_as_achieved),
                    this.getString(R.string.yes_continue),
                    "DetailGoalsActivity",
                    this,
                    View.OnClickListener {
                        this.realm.executeTransaction{ t ->
                            this.wishlist.isCompleted = true
                            t.copyToRealmOrUpdate(this.wishlist)

                            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
            )
        }
    }

    private fun calculatePercentage(): Float {
         return ((this.amount.toFloat().div(wishlist.amount.toString().toFloat())) * 100)
    }
}