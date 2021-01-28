package com.steadytech.impian.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import java.text.SimpleDateFormat

class InfoBottomSheetFragment(private val saving: Saving, private val listener : View.OnClickListener, private val isCompleted : Boolean) : BottomSheetDialogFragment() {

    private lateinit var inputAmount : EditText
    private lateinit var inputDescription : EditText
    private lateinit var inputDate : EditText

    private lateinit var buttonDelete : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.init(view)
        this.buttonDelete.setOnClickListener(listener)
    }

    private fun init(view: View) {
        this.inputAmount = view.findViewById(R.id.inputAmount)
        this.inputDate = view.findViewById(R.id.inputDate)
        this.inputDescription = view.findViewById(R.id.inputDescription)
        this.buttonDelete = view.findViewById(R.id.buttonDelete)

        this.inputDate.typeface = FontsHelper.INTER.regular(this.activity!!)
        this.inputAmount.typeface = FontsHelper.INTER.regular(this.activity!!)
        this.inputDescription.typeface = FontsHelper.INTER.regular(this.activity!!)
        this.buttonDelete.typeface = FontsHelper.INTER.regular(this.activity!!)

        if (this.isCompleted){
            this.buttonDelete.visibility = View.GONE
        }else{
            this.buttonDelete.visibility = View.VISIBLE
        }

        var sdf = SimpleDateFormat("yyyymmdd")
        val date = sdf.parse(this.saving.savingDate)

        sdf = SimpleDateFormat("EEEE, dd/MM/yyyy")


        this.inputDescription.setText(this.saving.description)
        this.inputAmount.setText(this.saving.amount.toString())
        this.inputDate.setText(sdf.format(date))
    }
}