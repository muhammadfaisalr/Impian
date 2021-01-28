package com.steadytech.impian.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steadytech.impian.R
import com.steadytech.impian.activity.AddBalanceActivity
import com.steadytech.impian.activity.CreateGoalsActivity
import com.steadytech.impian.helper.FontsHelper


class CreateGoalsBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var textCreateGoals : TextView
    lateinit var textAddBalance : TextView

    lateinit var layoutCreateGoals : LinearLayout
    lateinit var layoutAddBalance : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_goals_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.init(view)
    }

    private fun init(view: View) {
        this.textAddBalance = view.findViewById(R.id.textAddBalance)
        this.textAddBalance.typeface = FontsHelper.INTER.regular(this.activity as AppCompatActivity)

        this.textCreateGoals = view.findViewById(R.id.textCreateGoals)
        this.textCreateGoals.typeface = FontsHelper.INTER.regular(this.activity as AppCompatActivity)

        this.layoutCreateGoals = view.findViewById(R.id.layoutCreateGoals)
        this.layoutAddBalance = view.findViewById(R.id.layoutAddBalance)

        this.layoutCreateGoals.setOnClickListener(this)
        this.layoutAddBalance.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == this.layoutCreateGoals){
            startActivity(Intent(this.activity, CreateGoalsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            activity!!.finish()
        }else{
            startActivity(Intent(this.activity, AddBalanceActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            activity!!.finish()
        }
    }
}