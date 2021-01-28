package com.steadytech.impian.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.steadytech.impian.R
import com.steadytech.impian.activity.DetailGoalsActivity
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*


class WishlistAdapter(
    private val wishlists: RealmResults<Wishlist>,
    private val activity: Activity,
    private var realm : Realm = Realm.getDefaultInstance()
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return wishlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wishlists[position]!!, activity, realm)
        holder.itemView.setOnClickListener {
            activity.startActivity(Intent(activity, DetailGoalsActivity::class.java).putExtra("id", this.wishlists[position]?.id))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textDuration: TextView = itemView.findViewById(R.id.textDuration)
        private val textTargetAmount: TextView = itemView.findViewById(R.id.textTargetAmount)
        private val textCurrentAmount: TextView = itemView.findViewById(R.id.textCurrentAmount)
        private val textCurrentProgress: TextView = itemView.findViewById(R.id.textCurrentProgress)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val imageIndicator: ImageView = itemView.findViewById(R.id.imageIndicator)

        @SuppressLint("SetTextI18n")
        fun bind(
            wishlist: Wishlist,
            activity: Activity,
            realm: Realm
        ) {
            this.textTitle.typeface = FontsHelper.INTER.medium(activity)
            this.textTitle.text = wishlist.name

            val sdfEnd = SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH)

            val prettyTime = PrettyTime(Locale("id", "id"))

            this.textDuration.typeface = FontsHelper.INTER.regular(activity)
            this.textDuration.text = prettyTime.format(sdfEnd.parse(wishlist.endDate))

            this.textTargetAmount.typeface = FontsHelper.INTER.light(activity)
            this.textTargetAmount.text = GeneralHelper.currencyFormat(wishlist.amount)

            this.textCurrentAmount.typeface = FontsHelper.INTER.light(activity)
            this.textCurrentAmount.text = GeneralHelper.currencyFormat(this.currentBalance(wishlist, realm))

            val percentage = this.calculatePercentage(wishlist, realm).toInt()

            if (percentage > 100){
                this.progressBar.setProgress(100, true)
                this.textCurrentProgress.text = "100%"
            }else{
                this.progressBar.setProgress(this.calculatePercentage(wishlist, realm).toInt(), true)
                this.textCurrentProgress.text = this.progressBar.progress.toString() + "%"
            }


            this.textCurrentProgress.typeface = FontsHelper.INTER.medium(activity)

            if (wishlist.isCompleted){
                this.imageIndicator.visibility = View.VISIBLE
            }

        }

        private fun currentBalance(
            wishlist: Wishlist,
            realm: Realm
        ): Long {
            val saving = realm.where<Saving>().equalTo("targetId", wishlist.id).findAll()
            var currentAmount = 0L
            for (i in saving){
                currentAmount += i.amount
            }

            return currentAmount
        }
         private fun calculatePercentage(
            wishlist: Wishlist,
            realm: Realm
        ): Float {
            val targetAmount = wishlist.amount.toString().toFloat()
            return ((this.currentBalance(wishlist, realm).div(targetAmount)) * 100)
        }
    }
}