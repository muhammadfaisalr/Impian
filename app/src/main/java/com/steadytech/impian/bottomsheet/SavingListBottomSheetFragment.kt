package com.steadytech.impian.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steadytech.impian.R
import com.steadytech.impian.adapter.SavingAdapter
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.helper.DatabaseHelper
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.model.realm.Saving
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class SavingListBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var textSaving : TextView

    private lateinit var imageClose : ImageView

    private lateinit var recyclerView : RecyclerView

    private lateinit var database : AppDatabase

    private lateinit var daoSaving : DaoSaving

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.init(view)
        this.data()
    }


    private fun init(view: View) {
        this.textSaving = view.findViewById(R.id.textSaving)
        this.textSaving.typeface = FontsHelper.INTER.regular(this.activity as AppCompatActivity)
        this.database = DatabaseHelper.localDb(this.activity!!)
        this.daoSaving = this.database.daoSaving()

        this.imageClose = view.findViewById(R.id.imageClose)

        this.recyclerView = view.findViewById(R.id.recyclerList)
        this.recyclerView.addItemDecoration(DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL))
        this.recyclerView.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)

        FontsHelper.INTER.bold(requireActivity(), this.textSaving)
    }

    private fun data(){
        val results = this.daoSaving.getAll()
        this.recyclerView.adapter = SavingAdapter(results, this.activity as AppCompatActivity)
    }
}