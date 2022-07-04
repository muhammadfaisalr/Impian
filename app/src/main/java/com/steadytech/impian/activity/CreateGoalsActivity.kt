package com.steadytech.impian.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.CategoriesBottomSheetDialogFragment
import com.steadytech.impian.custom.Loading
import com.steadytech.impian.custom.RupiahEditText
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.databinding.ActivityCreateGoalsBinding
import com.steadytech.impian.helper.*
import java.text.SimpleDateFormat
import java.util.*


class  CreateGoalsActivity : AppCompatActivity(), View.OnClickListener,
    CalendarView.OnDateChangeListener {

    private lateinit var textBack: TextView
    private lateinit var textTitleCalendar: TextView

    private lateinit var calendar: CalendarView

    private lateinit var inputCategory: TextInputEditText
    private lateinit var inputName: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputBudget: RupiahEditText

    private lateinit var exfabSave: ExtendedFloatingActionButton

    private lateinit var binding: ActivityCreateGoalsBinding

    private var targetDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCreateGoalsBinding.inflate(this.layoutInflater)
        super.setContentView(this.binding.root)
        this.supportActionBar!!.hide()

        this.init()
    }

    private fun init() {
        this.textBack = this.binding.textBack
        this.textTitleCalendar = this.binding.textHintCalendar
        this.inputCategory = this.binding.inputCategory
        this.inputDescription = this.binding.inputDescription
        this.inputName = this.binding.inputGoalsName
        this.inputBudget = this.binding.inputBudget
        this.calendar = this.binding.calendar
        this.exfabSave = this.binding.exfabSave

        FontsHelper.INTER.medium(this, this.textBack, this.textTitleCalendar)
        FontsHelper.INTER.regular(this, this.inputName, this.inputCategory, this.inputBudget)

        this.settingCalendar()

        this.textBack.setOnClickListener(this)
        this.inputCategory.setOnClickListener(this)
        this.exfabSave.setOnClickListener(this)
        this.calendar.setOnDateChangeListener(this)
    }

    private fun settingCalendar() {
        this.calendar.minDate = Date().time
    }

    override fun onClick(v: View?) {
        if (v == this.textBack) {
            this.back()
        } else if (v == this.inputCategory) {
            this.category()
        } else if (v == this.exfabSave) {
            this.save()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun save() {
        var isPassed = GeneralHelper.validateInputNotEmpty(
            this.inputBudget,
            this.inputCategory,
            this.inputName,
        )

        if (isPassed) {
            isPassed = this.targetDate.isNotEmpty()
        }

        if (isPassed) {
            isPassed = this.inputBudget.text.toString() != "0"
        }

        if (isPassed) {
            val loading = Loading(this)
            loading.setCancelable(false)
            loading.show()

            val isFreeMode = SharedPreferenceHelper.isFreeMode(this)

            val name = this.inputName.text.toString()
            val budget = this.inputBudget.text.toString().replace(".", "")
            val description = this.inputDescription.text.toString()
            val category = this.inputCategory.text.toString()

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDate = sdf.format(Date())

            val db = DatabaseHelper.localDb(this)
            val entityWishlist = EntityWishlist(
                id = null,
                name = name,
                amount = budget.toLong(),
                description = description,
                startDate = currentDate,
                endDate = this.targetDate,
                reminderInterval = Constant.REMINDER_INTERVAL.DAY,
                isCompleted = false,
                category = category,
                isSynchronized = Constant.NO
            )

            db.daoWishlist().insert(entityWishlist)

//            AlarmHelper.setAlarm(this, 44, 13, 49   , 0)

            if (isFreeMode or UserHelper.isAnonymous(this)){
                //Jika menggunakan mode gratis atau Tanpa Login
                loading.dismiss()
                startActivity(Intent(this, SuccessActivity::class.java))
                finish()
                return
            }

            //Jika tidak menggunakan mode gratis dan Login Menggunakan Akun
            DatabaseHelper.FIREBASE.getImpianPath(this).child(entityWishlist.id.toString()).setValue(entityWishlist).addOnSuccessListener {
                Log.d(CreateGoalsActivity::class.java.simpleName, "Success set value to Firebase")

                entityWishlist.isSynchronized = Constant.YES
                DatabaseHelper.localDb(this).daoWishlist().update(entityWishlist)
                loading.dismiss()

                startActivity(Intent(this, SuccessActivity::class.java))
                finish()
            }.addOnFailureListener{
                Log.d(CreateGoalsActivity::class.java.simpleName, "Fail to set value in Firebase!\nError Message : ${it.message}")
                it.printStackTrace()
                BottomSheets.error(
                        title = this.getString(R.string.fail_to_save_data),
                        subtitle = it.message!!,
                        useTitle = true,
                        useSubTitle =  true,
                        buttonMessage = this. getString(R.string.ok),
                        TAG = CreateGoalsActivity::class.java.simpleName,
                        activity =  this
                )
            }

        } else {
            BottomSheets.error(
                title = this.getString(R.string.can_t_create_goals),
                subtitle = this.getString(R.string.subtitle_data_is_not_filled),
                useTitle = true,
                useSubTitle = true,
                buttonMessage = this.getString(R.string.ok),
                TAG = CreateGoalsActivity::class.java.simpleName,
                activity = this
            )
        }
    }

    private fun category() {
        val category = CategoriesBottomSheetDialogFragment { entityImpianCategory, i ->
            this.inputCategory.setText(entityImpianCategory.name)
            this.inputCategory.setCompoundDrawablesWithIntrinsicBounds(
                this.resources.getDrawable(
                    entityImpianCategory.image!!, null
                ), null, null, null
            )
        }
        category.show(this.supportFragmentManager, CreateGoalsActivity::class.java.simpleName)
    }

    private fun back() {
        finish()
    }


    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        val currentMonth = month + 1
        this.targetDate = "$dayOfMonth/$currentMonth/$year"
    }
}