package com.steadytech.impian.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.steadytech.impian.R
import com.steadytech.impian.bottomsheet.CategoriesBottomSheetDialogFragment
import com.steadytech.impian.database.entity.EntityImpianCategory
import com.steadytech.impian.helper.FontsHelper


class CategoriesAdapter     (
    private val activity : Activity,
    private val categoriesBottomSheetDialogFragment: CategoriesBottomSheetDialogFragment,
    private val entitiesCategoryImpian : List<EntityImpianCategory>,
    private val listener : (EntityImpianCategory, Int) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textView : TextView
        lateinit var imageView : ImageView

        fun bind(activity: Activity, entityCategoryImpian: EntityImpianCategory) {
            this.textView = itemView.findViewById(R.id.textView)
            this.imageView = itemView.findViewById(R.id.imageView)

            this.textView.text = entityCategoryImpian.name
            this.imageView.setImageResource(entityCategoryImpian.image!!)

            FontsHelper.INTER.regular(activity, this.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entityCategory = entitiesCategoryImpian[position]
        holder.bind(activity, entityCategory)
        holder.itemView.setOnClickListener {
            listener(entityCategory, position)
            categoriesBottomSheetDialogFragment.dismiss()
        }
    }

    override fun getItemCount(): Int {
        return this.entitiesCategoryImpian.size
//        return 0
    }
}