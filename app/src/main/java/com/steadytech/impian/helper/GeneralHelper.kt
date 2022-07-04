package com.steadytech.impian.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.steadytech.impian.R
import com.steadytech.impian.database.dao.DaoWishlist
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class GeneralHelper {
    companion object {

        fun changeStatusBarColor(activity: Activity, color: Int) {
            val window: Window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, color)
        }

        fun currencyFormat(amount: Long): String {
            return NumberFormat
                .getCurrencyInstance(Locale("id", "id"))
                .format(amount)
                .replace(",00", "")
        }

        fun textAvatar(text: String) : String{
            val datas = text.split(" ")
            val result = StringBuilder()

            for (data in datas){
                if (result.length < 3){
                    result.append(data[0])
                }
            }

            return result.toString()
        }

        fun makeClickable(listener: View.OnClickListener, vararg views: View) {
            for (i in views) {
                i.setOnClickListener(listener)
            }
        }

        fun countDaysBetweenTwoCalendar(calendarStart: Calendar, calendarEnd: Calendar) : Int{
            val millionSeconds = calendarEnd.timeInMillis - calendarStart.timeInMillis
            return (millionSeconds / (1000.0 * 60 * 60 * 24)).roundToInt()
        }

        fun calculateRecommendationSaving(goalsName: String, daoWishlist: DaoWishlist): String {
            val wishlist = daoWishlist.getByName(goalsName)

            val start = Calendar.getInstance()
            val end = Calendar.getInstance()

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            start.time = sdf.parse(wishlist!!.startDate)

            val sdfEnd = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            end.time = sdfEnd.parse(wishlist!!.endDate)

            var daysLeft = this.countDaysBetweenTwoCalendar(start, end)

            if (daysLeft == 0) {
                // Handle if the target is targeted in sameday
                daysLeft = 1
            }
            val recommendationLong = wishlist.amount!! / daysLeft
            
            return currencyFormat(recommendationLong)
        }


        fun copy(text: String?, activity: Activity) {
            val clipboardManager =
                activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copy", text)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(activity, "Copied To Clipboard!", Toast.LENGTH_SHORT).show()
        }

        fun dot(): String? {
            return "\u2022 "
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun setImageImpianCategory(context: Context, category: String, imageView: ImageView) {
            when (category) {
                context.getString(R.string.wedding) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_favorite_24))
                }
                context.getString(R.string.vacation) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_airplane_ticket_24))
                }
                context.getString(R.string.electronic) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_headphones_24))
                }
                context.getString(R.string.home) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_maps_home_work_24))
                }
                context.getString(R.string.health) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_local_hospital_24))
                }
                context.getString(R.string.education) -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_school_24))
                }
                else -> {
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_round_space_dashboard_24))
                }
            }
        }

        fun verticalDivider(context: Context) : DividerItemDecoration{
            val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.item_divider)!!)

            return itemDecorator
        }
        fun horizontalDivider(context: Context) : DividerItemDecoration{
            val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.item_divider)!!)

            return itemDecorator
        }

        fun validateInputNotEmpty(vararg editText: EditText) : Boolean {
            var isPassed = true

            for (input in editText){
                if (input.text.isEmpty()){
                    isPassed = false
                }

                if (input.text.toString() == ""){
                    isPassed = false
                }
            }

            return isPassed
        }
    }
}