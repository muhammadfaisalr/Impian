package com.steadytech.impian.custom

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.text.NumberFormat
import java.util.*

class RupiahEditText : AppCompatEditText {
    private val editText = this@RupiahEditText

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    val value: Long
        get() {
            return Objects.requireNonNull(editText.text).toString().replace(".", "")
                .toLong()
        }

    fun init() {
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.setText("0")
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        editText.filters = arrayOf<InputFilter>(LengthFilter(19))
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                editText.removeTextChangedListener(this)
                if (s.toString() != "") {
                    if (s.toString().substring(0, 1) == "0") {
                        //angka pertama 0
                        if (s.toString().length > 1) {
                            //panjang angka lebih dari 1
                            if (s.toString().substring(0, 1) == "0" && s.toString()
                                    .substring(1, 2) !== "0"
                            ) {
                                //angka pertama 0 dan angka kedua tidak 0, hilangkan angka 0 dikiri
                                editText.setText(
                                    formatCurrency(
                                        StringBuilder(s.toString()).deleteCharAt(
                                            0
                                        ).toString()
                                            .toLong()
                                    )
                                )
                            } else if (s.toString().substring(0, 1) != "0") {
                                //angka pertama tidak 0
                                editText.setText(formatCurrency(s.toString().toLong()))
                            } else {
                                //angka kedua 0
                                editText.setText("0")
                            }
                        } else {
                            //panjang angka adalah 1
                            editText.setText(formatCurrency(s.toString().toLong()))
                        }
                    } else {
                        //angka pertama tidak 0
                        val cleanString = s.toString().replace(".", "")
                        editText.setText(formatCurrency(cleanString.toLong()))
                    }
                } else {
                    editText.setText("0")
                }
                editText.setSelection(editText.length())
                editText.addTextChangedListener(this)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun validateValue(inputed: String): String {
        return if (inputed != "") {
            if (inputed.substring(0, 1) == "0") {
                //angka pertama 0
                if (inputed.length > 1) {
                    //panjang angka lebih dari 1
                    if (inputed.substring(0, 1) == "0" && inputed.substring(1, 2) != "0"
                    ) {
                        //angka pertama 0 dan angka kedua tidak 0, hilangkan angka 0 dikiri
                        formatCurrency(
                            StringBuilder(inputed).deleteCharAt(
                                0
                            ).toString()
                                .toLong()
                        )
                    } else if (inputed.substring(0, 1) != "0") {
                        //angka pertama tidak 0
                        formatCurrency(inputed.toLong())
                    } else {
                        //angka kedua 0
                        "0"
                    }
                } else {
                    //panjang angka adalah 1
                    formatCurrency(inputed.toLong())
                }
            } else {
                //angka pertama tidak 0
                val cleanString = inputed.replace(".", "")
                formatCurrency(cleanString.toLong())
            }
        } else {
            "0"
        }
    }

    private fun formatCurrency(amount: Long): String {
        val format =
            NumberFormat.getNumberInstance(Locale.GERMANY)
        return format.format(amount)
    }
}
