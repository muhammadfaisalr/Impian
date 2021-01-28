package com.steadytech.impian.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.DigitsKeyListener
import android.text.method.TextKeyListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.button.MaterialButton
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.ConfirmationBottomSheetFragment
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random


class CreateGoalsActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var textTitle: TextView
    private lateinit var textStep1: TextView
    private lateinit var textStep2: TextView
    private lateinit var textStep3: TextView
    private lateinit var textDate: TextView

    private lateinit var inputAnswer: EditText
    private lateinit var inputAmount: EditText

    private lateinit var layoutStep: LinearLayout
    private lateinit var layoutAmount: LinearLayout

    private lateinit var buttonNext: MaterialButton
    private lateinit var buttonPrevious: MaterialButton

    private lateinit var progressBar: ProgressBar

    private var handler: Handler = Handler()
    private var step: Int = 1

    private lateinit var realm : Realm


    private var name: String = ""
    private var amount: String = ""
    private var date: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_create_goals)

        this.supportActionBar!!.hide()

        this.init()

        this.validate()

        this.buttonNext.setOnClickListener(this)
        this.buttonPrevious.setOnClickListener(this)
        this.textDate.setOnClickListener(this)
    }

    private fun validate() {

        if (step != 1) {
            this.layoutStep.visibility = View.GONE
        }

        when (step) {
            1 -> {
                this.handler.postDelayed({
                    this.step1()
                }, 500)
            }
            2 -> {
                this.handler.postDelayed({
                    this.step2()
                }, 500)
            }
            else -> {
                this.handler.postDelayed({
                    this.step3()
                }, 500)
            }
        }
    }


    private fun init() {
        this.realm = Realm.getDefaultInstance()

        this.progressBar = findViewById(R.id.progressBar)

        this.layoutAmount = findViewById(R.id.layoutAmount)

        this.textStep1 = findViewById(R.id.textStep1)
        this.textStep1.typeface = FontsHelper.INTER.light(this)

        this.textStep2 = findViewById(R.id.textStep2)
        this.textStep2.typeface = FontsHelper.INTER.light(this)

        this.textStep3 = findViewById(R.id.textStep3)
        this.textStep3.typeface = FontsHelper.INTER.light(this)

        this.textTitle = findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this)

        this.textDate = findViewById(R.id.textDate)
        this.textDate.typeface = FontsHelper.INTER.regular(this)

        this.inputAnswer = findViewById(R.id.inputAnswer)
        this.inputAnswer.typeface = FontsHelper.INTER.regular(this)

        this.inputAmount = findViewById(R.id.inputAmount)
        this.inputAmount.typeface = FontsHelper.INTER.regular(this)

        this.buttonNext = findViewById(R.id.buttonNext)
        this.buttonNext.typeface = FontsHelper.INTER.regular(this)

        this.buttonPrevious = findViewById(R.id.buttonPrevious)
        this.buttonPrevious.typeface = FontsHelper.INTER.regular(this)

        this.layoutStep = findViewById(R.id.layoutStep1)
    }

    override fun onClick(v: View?) {
        if (v == this.buttonNext) {
            this.next()
        } else if (v == this.buttonPrevious) {
            this.previous()
        } else if (v == this.textDate) {
            if (this.step == 3) {
               this.showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000

        val dialog = DatePickerDialog.Builder(
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).setMinDate(calendar).build()

        dialog.show(this.supportFragmentManager, Constant.TAG.CreateGoalsActivity)
    }


    private fun next() {
        when (this.step) {
            1 -> {
                if (this.inputAnswer.text.isNotEmpty()){
                    this.processStep()
                }else{
                    Toast.makeText(this, "Kamu Belum Masukkan Jawaban!", Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                if (this.inputAmount.text.isNotEmpty()){
                    this.processStep()
                }else{
                    Toast.makeText(this, "Kamu Belum Masukkan Jawaban!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                if (this.textDate.text.toString() == "Masukkan Jawaban Disini"){
                    Toast.makeText(this, "Kamu Belum Masukkan Jawaban!", Toast.LENGTH_SHORT).show()
                }else{
                    this.finishStep()
                }
            }
        }
    }

    private fun finishStep() {
        this.date = this.textDate.text.toString()

        val bundle = Bundle()
        bundle.putString("name", this.name)
        bundle.putString("amount", this.amount)
        bundle.putString("date", this.date)

        val confirmationBottomSheetFragment = ConfirmationBottomSheetFragment(
            View.OnClickListener {
                this.realm.executeTransaction {

                    val wishlist = it.createObject(Wishlist::class.java, Random.nextInt().toLong())
                    wishlist.startDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                    wishlist.endDate = this.date.format(DateTimeFormatter.BASIC_ISO_DATE)
                    wishlist.name = this.name
                    wishlist.amount = this.amount.replace(".", "").toLong()
                    wishlist.reminderInterval = Constant.REMINDER_INTERVAL.DAY
                    wishlist.description = "Tidak Ada Deskripsi"

                    it.copyToRealmOrUpdate(wishlist)

                }
                startActivity(Intent(this, SuccessActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
            })
        confirmationBottomSheetFragment.arguments = bundle
        confirmationBottomSheetFragment.show(this.supportFragmentManager, Constant.TAG.CreateGoalsActivity)
    }



    private fun processStep() {
        this.step++
        YoYo.with(Techniques.FadeOut).playOn(this.layoutStep)
        this.handler.postDelayed({
            this.validate()
        }, 500)
    }

    private fun step3() {
        if (step == 3) {

            this.inputAmount.visibility = View.GONE
            this.inputAnswer.visibility = View.GONE
            this.textDate.visibility = View.VISIBLE
            this.layoutAmount.visibility = View.GONE

            this.amount = this.inputAmount.text.toString()

            this.textStep1.setTextColor(this.resources.getColor(R.color.text_primary))
            this.textStep2.setTextColor(this.resources.getColor(R.color.text_primary))
            this.textStep3.setTextColor(this.resources.getColor(R.color.black))
            this.textStep3.typeface = FontsHelper.INTER.medium(this)
            this.textStep2.typeface = FontsHelper.INTER.light(this)
            this.textStep1.typeface = FontsHelper.INTER.light(this)

            this.progressBar.setProgress(100, true)
            this.amount = this.inputAmount.text.toString()

            this.layoutStep.visibility = View.VISIBLE
            this.buttonPrevious.visibility = View.VISIBLE
            YoYo.with(Techniques.FadeIn).playOn(this.layoutStep)
            this.textTitle.text = "Kapan Kamu Ingin " + this.name + " Tercapai?"
            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            this.buttonNext.text = "Selesai"
        }
    }

    private fun step2() {
        if (step == 2) {
            this.inputAmount.visibility = View.VISIBLE
            this.layoutAmount.visibility = View.VISIBLE
            this.inputAnswer.visibility = View.GONE
            this.textDate.visibility = View.GONE
            this.name = this.inputAnswer.text.toString()

            this.textStep1.setTextColor(this.resources.getColor(R.color.text_primary))
            this.textStep2.setTextColor(this.resources.getColor(R.color.black))
            this.textStep3.setTextColor(this.resources.getColor(R.color.text_primary))

            this.textStep3.typeface = FontsHelper.INTER.light(this)
            this.textStep2.typeface = FontsHelper.INTER.medium(this)
            this.textStep1.typeface = FontsHelper.INTER.light(this)

            this.progressBar.setProgress(50, true)

            this.layoutStep.visibility = View.VISIBLE
            this.buttonPrevious.visibility = View.VISIBLE
            YoYo.with(Techniques.FadeIn).playOn(this.layoutStep)
            this.textTitle.text = "Berapa Biaya Yang Kamu Butuhkan\nUntuk " + this.name + " ?"

            this.inputAmount.setText(this.amount)

            this.inputAmount.keyListener = DigitsKeyListener.getInstance("0123456789")
        }
    }

    private fun step1() {
        if (step == 1) {

            this.inputAmount.visibility = View.GONE
            this.inputAnswer.visibility = View.VISIBLE
            this.textDate.visibility = View.GONE
            this.layoutAmount.visibility = View.GONE

            this.progressBar.setProgress(0, true)

            this.textStep3.typeface = FontsHelper.INTER.light(this)
            this.textStep2.typeface = FontsHelper.INTER.light(this)
            this.textStep1.typeface = FontsHelper.INTER.medium(this)

            this.textStep1.setTextColor(this.resources.getColor(R.color.black))
            this.textStep2.setTextColor(this.resources.getColor(R.color.text_primary))
            this.textStep3.setTextColor(this.resources.getColor(R.color.text_primary))

            this.buttonPrevious.visibility = View.GONE

            this.layoutStep.visibility = View.VISIBLE
            YoYo.with(Techniques.FadeIn).playOn(this.layoutStep)


            this.textTitle.text = "Apa Yang Kamu Impikan?"

            this.inputAnswer.setText(this.name)

            this.inputAnswer.isClickable = false
            this.inputAnswer.keyListener = TextKeyListener(TextKeyListener.Capitalize.WORDS, false)
        }
    }


    private fun previous() {
        this.step--
        this.validate()
        YoYo.with(Techniques.FadeOut).playOn(this.layoutStep)
    }

    override fun onDateSet(
        dialog: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val month = monthOfYear + 1
        this.textDate.text = "$dayOfMonth/$month/$year"
    }

    override fun onBackPressed() {
        if (this.step != 1){
            this.previous()
        }else{
            super.onBackPressed()
        }
    }
}