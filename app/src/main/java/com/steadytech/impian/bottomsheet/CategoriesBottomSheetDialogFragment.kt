package com.steadytech.impian.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steadytech.impian.R
import com.steadytech.impian.adapter.CategoriesAdapter
import com.steadytech.impian.database.entity.EntityImpianCategory
import com.steadytech.impian.databinding.FragmentCategoriesBottomSheetDialogBinding
import com.steadytech.impian.helper.DatabaseHelper
import com.steadytech.impian.helper.FontsHelper
import java.util.*

class CategoriesBottomSheetDialogFragment(
    private val listener : (EntityImpianCategory, Int) -> Unit
) :
    BottomSheetDialogFragment() {

    private lateinit var textTitle: TextView
    private lateinit var recyclerList: RecyclerView

    private lateinit var binding: FragmentCategoriesBottomSheetDialogBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.binding = FragmentCategoriesBottomSheetDialogBinding.inflate(inflater)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.init()
    }

    private fun init() {
        val db = DatabaseHelper.localDb(this.activity!!)

        this.textTitle = this.binding.textTitle
        this.recyclerList = this.binding.recyclerList

        FontsHelper.INTER.medium(this.activity!!, this.textTitle)

        this.recyclerList.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        this.recyclerList.adapter = CategoriesAdapter(
            this.activity!!,
            this,
            db.daoCategory().getAll(),
            this.listener
        )
    }
}