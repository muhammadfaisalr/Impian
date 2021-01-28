package com.steadytech.impian.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper


class ErrorBottomSheetFragment() : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textSubtitle : TextView
    private lateinit var buttonOK : TextView

    private var isUseTitle = true
    private var isUseSubtitle = true
    private var title = ""
    private var subtitle = ""
    private var buttonMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.data()

        this.init(view)

        this.buttonOK.setOnClickListener(this)
    }

    private fun data() {
        this.title = this.arguments!!.getString("title").toString()
        this.subtitle = this.arguments!!.getString("subtitle").toString()
        this.isUseSubtitle = this.arguments!!.getBoolean("useSubtitle")
        this.isUseTitle = this.arguments!!.getBoolean("useTitle")
        this.buttonMessage = this.arguments!!.getString("buttonMessage").toString()
    }

    private fun init(view: View) {
        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.medium(this.activity!!)
        this.textTitle.text = title

        this.textSubtitle = view.findViewById(R.id.textSubtitle)
        this.textSubtitle.typeface = FontsHelper.INTER.regular(this.activity!!)
        this.textSubtitle.text = this.subtitle

        if (!isUseTitle){
            this.textTitle.visibility = View.GONE
        }

        if (!isUseSubtitle){
            this.textTitle.visibility = View.GONE
        }

        this.buttonOK = view.findViewById(R.id.buttonOk)
        this.buttonOK.typeface = FontsHelper.INTER.regular(this.activity!!)
        this.buttonOK.text = this.buttonMessage
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}