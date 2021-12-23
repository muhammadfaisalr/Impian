package com.steadytech.impian.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.steadytech.impian.R
import com.steadytech.impian.model.realm.Menu
import io.realm.RealmResults

class MenuAdapter(private val menus : RealmResults<Menu>, val activity : Activity) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var textView : TextView
        lateinit var imageView : ImageView  

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(data : Menu, activity : Activity) {
            this.textView = itemView.findViewById(R.id.textView)
            this.textView.text = data.name

            this.imageView = itemView.findViewById(R.id.imageView)
            this.imageView.setImageDrawable(activity.getDrawable(data.icon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        val menu = menus[position]!!

        holder.bind(menu, activity)
    }
}