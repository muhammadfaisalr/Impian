package com.steadytech.impian.helper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.kotlin.where
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class GeneralHelper {
    companion object {
        fun currencyFormat(amount: Long): String {
            return NumberFormat.getCurrencyInstance(Locale("id", "id")).format(amount)
                .replace(",00", "")
        }

        fun hideKeyboard(activity: Activity) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view: View? = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun countDaysBetweenTwoCalendar(calendarStart: Calendar, calendarEnd: Calendar) : Int{
            val millionSeconds = calendarEnd.timeInMillis - calendarStart.timeInMillis
            return (millionSeconds / (1000.0 * 60 * 60 * 24)).roundToInt()
        }

        fun calculateRecommendationSaving(goalsName : String, realm : Realm): String {
            val wishlist = realm.where<Wishlist>().equalTo("name", goalsName).findFirst()

            val start = Calendar.getInstance()
            val end = Calendar.getInstance()

            val sdf = SimpleDateFormat("yyyymmdd", Locale.ENGLISH)
            start.time = sdf.parse(wishlist!!.startDate)

            val sdfEnd = SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH)
            end.time = sdfEnd.parse(wishlist!!.endDate)

            val daysLeft = this.countDaysBetweenTwoCalendar(start, end)

            val recommendationLong = wishlist.amount / daysLeft
            
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
    }
}