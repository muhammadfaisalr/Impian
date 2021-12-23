package com.steadytech.impian.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.model.Font
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper

class DescriptionBottomSheetFragment(var image : Int, var title : String, var description : String) : BottomSheetDialogFragment(),
    View.OnClickListener {

    private lateinit var textTitle : TextView
    private lateinit var textDescription : TextView

    private lateinit var imageIcon : ImageView

    private lateinit var buttonOk : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.init(view)
        this.buttonOk.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(view: View) {
        this.textTitle = view.findViewById(R.id.textTitle)
        this.textTitle.typeface = FontsHelper.INTER.bold(this.activity as AppCompatActivity)
        this.textTitle.text = title

        this.textDescription = view.findViewById(R.id.textDescription)
        this.textDescription.typeface = FontsHelper.INTER.regular(this.activity as AppCompatActivity)
        this.textDescription.text = description

        this.imageIcon = view.findViewById(R.id.imageIcon)
        this.imageIcon.setImageDrawable((this.activity as AppCompatActivity).getDrawable(image))

        this.buttonOk = view.findViewById(R.id.buttonOk)
        this.buttonOk.typeface = FontsHelper.INTER.medium(this.activity as AppCompatActivity)
    }

    override fun onClick(v: View?) {
        if (v == this.buttonOk){
            this.dismiss()
        }
    }
}