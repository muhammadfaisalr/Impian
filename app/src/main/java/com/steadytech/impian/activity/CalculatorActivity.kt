package com.steadytech.impian.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.steadytech.impian.databinding.ActivityCalculatorBinding
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CalculatorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var numbers: ArrayList<String>

    private var data = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCalculatorBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.numbers = ArrayList()
        this.numbers.add("1")
        this.numbers.add("2")
        this.numbers.add("3")
        this.numbers.add("4")
        this.numbers.add("5")
        this.numbers.add("6")
        this.numbers.add("7")
        this.numbers.add("8")
        this.numbers.add("9")

        this.binding.apply {
            this.textAmount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val s = p0!!
                    Log.d(CalculatorActivity::class.java.simpleName, "${GeneralHelper.currencyFormat(s.toString().toLong())} Currency Format")
                    data = GeneralHelper.currencyFormat(
                        s.toString()
                            .replace("Rp","")
                            .replace(".","")
                            .toLong()
                    )
                }

            })

            FontsHelper.INTER.bold(this@CalculatorActivity, this.textAmount)
            FontsHelper.INTER.medium(
                this@CalculatorActivity,
                this.text0,
                this.text1,
                this.text2,
                this.text3,
                this.text4,
                this.text5,
                this.text6,
                this.text7,
                this.text8,
                this.text9,
                this.text0,
                this.text000,
            )

            GeneralHelper.makeClickable(
                this@CalculatorActivity,
                this.text0,
                this.text1,
                this.text2,
                this.text3,
                this.text4,
                this.text5,
                this.text6,
                this.text7,
                this.text8,
                this.text9,
                this.text0,
                this.text000,
            )
        }
    }

    override fun onClick(p0: View?) {
        when(p0) {
            this.binding.text0 -> {
                for (number in numbers) {
                    if (data.contains(number)) {
                        data = "${data}0"
                        break
                    }else if (data == "") {
                        data = "0"
                    }
                }
            }
            this.binding.text1 -> {
                data = if (data == "0") {
                    "1"
                }else{
                    "${data}1"
                }
            }
            this.binding.text2 -> {
                data = if (data == "0") {
                    "2"
                }else{
                    "${data}2"
                }
            }
            this.binding.text3 -> {
                data = if (data == "0") {
                    "3"
                }else{
                    "${data}3"
                }
            }
            this.binding.text4 -> {
                data = if (data == "0") {
                    "4"
                }else{
                    "${data}4"
                }
            }
            this.binding.text5 -> {
                data = if (data == "0") {
                    "5"
                }else{
                    "${data}5"
                }
            }
            this.binding.text6 -> {
                data = if (data == "0") {
                    "6"
                }else{
                    "${data}6"
                }
            }
            this.binding.text7 -> {
                data = if (data == "0") {
                    "7"
                }else{
                    "${data}7"
                }
            }
            this.binding.text8 -> {
                data = if (data == "0") {
                    "8"
                }else{
                    "${data}8"
                }
            }
            this.binding.text9 -> {
                data = if (data == "0") {
                    "9"
                }else{
                    "${data}9"
                }
            }
            this.binding.text000 -> {
                for (number in numbers) {
                    if (data.contains(number)) {
                        data = "${data}000"
                        break
                    }else if (data == "") {
                        data = "${data}0"
                    }
                }
            }
        }

        this.binding.textAmount.text = data
    }
}