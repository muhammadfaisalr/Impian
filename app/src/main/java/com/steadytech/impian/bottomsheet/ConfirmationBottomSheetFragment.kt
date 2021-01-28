package com.steadytech.impian.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.steadytech.impian.R
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper


class ConfirmationBottomSheetFragment(
    var listenerSave: View.OnClickListener
) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var textTitle: TextView
    private lateinit var textDescription: TextView

    private lateinit var buttonCancel: MaterialButton
    private lateinit var buttonSave: MaterialButton

    var name: String = ""
    var amount: String = ""
    var date: String = ""

    var title: String = ""
    var subtitle: String = ""
    var buttonMessage: String = ""
    var TAG: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.data()

        this.init(view)

        this.description()

        this.buttonSave.setOnClickListener(listenerSave)
        this.buttonCancel.setOnClickListener(this)
    }


    private fun data() {
        this.name = this.arguments!!.getString("name").toString()
        this.amount = this.arguments!!.getString("amount").toString()
        this.buttonMessage = this.arguments!!.getString("positiveMessage").toString()
        this.date = this.arguments!!.getString("date").toString()
        this.TAG = this.tag.toString()
        this.title = this.arguments!!.getString("title").toString()
        this.subtitle = this.arguments!!.getString("subtitle").toString()
    }

    private fun init(view: View) {
        this.buttonCancel = view.findViewById(R.id.buttonCancel)
        this.buttonCancel.typeface = FontsHelper.INTER.regular(this.activity!!)

        this.buttonSave = view.findViewById(R.id.buttonSave)
        this.buttonSave.typeface = FontsHelper.INTER.regular(this.activity!!)

        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this.activity!!)

        this.textDescription = view.findViewById(R.id.textDescription)
        this.textDescription.typeface = FontsHelper.INTER.regular(this.activity!!)
    }


    private fun description() {
        if (TAG == Constant.TAG.CreateGoalsActivity){

            val nameSpannable = SpannableStringBuilder(this.name)
            nameSpannable.setSpan(ForegroundColorSpan(this.resources.getColor(R.color.yellow_800)), 0, nameSpannable.length, 0)
            nameSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, nameSpannable.length, 0)

            val amountSpannable = SpannableStringBuilder(GeneralHelper.currencyFormat(this.amount.replace(".", "").toLong()))
            amountSpannable.setSpan(ForegroundColorSpan(this.resources.getColor(R.color.yellow_800)), 0, amountSpannable.length, 0)
            amountSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, amountSpannable.length, 0)


            val dateSpannable = SpannableStringBuilder(this.date)
            dateSpannable.setSpan(ForegroundColorSpan(this.resources.getColor(R.color.yellow_800)), 0, dateSpannable.length, 0)
            dateSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, dateSpannable.length, 0)


            this.textDescription.text = TextUtils.concat("Impian ", nameSpannable, " Membutuhkan Biaya ", amountSpannable ,"  Yang Harus Tercapai Sebelum Tanggal ", dateSpannable ," Akan Disimpan")
        }else{
            this.textTitle.text = this.title
            this.textDescription.text = this.subtitle
            this.buttonSave.text = this.buttonMessage
        }

    }

    override fun onClick(v: View?) {
        if (v == this.buttonCancel){
            dismiss()
        }
    }
}