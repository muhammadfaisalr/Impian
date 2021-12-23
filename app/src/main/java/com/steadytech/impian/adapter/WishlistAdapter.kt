package com.steadytech.impian.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.steadytech.impian.R
import com.steadytech.impian.activity.DetailGoalsActivity
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.dao.DaoSaving
import com.steadytech.impian.database.entity.EntityWishlist
import com.steadytech.impian.helper.DatabaseHelper
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
    private val wishlists: List<EntityWishlist>,
    private val activity: Activity,
    private var database : AppDatabase = DatabaseHelper.localDb(activity)
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
        holder.bind(activity, wishlists[position], activity, database.daoSaving())
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
        private val imageThumbnail: ImageView = itemView.findViewById(R.id.imageThumbnail)

        private val cardThumbnail : CardView = itemView.findViewById(R.id.cardView)

        @SuppressLint("SetTextI18n")
        fun bind(
            context : Context,
            wishlist: EntityWishlist,
            activity: Activity,
            daoSaving: DaoSaving
        ) {
            this.textTitle.typeface = FontsHelper.INTER.medium(activity)
            this.textTitle.text = wishlist.name

            val sdfEnd = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            val prettyTime = PrettyTime(Locale("id", "id"))

            this.textDuration.typeface = FontsHelper.INTER.regular(activity)
            this.textDuration.text = prettyTime.format(sdfEnd.parse(wishlist.endDate))

            this.textTargetAmount.typeface = FontsHelper.INTER.light(activity)
            this.textTargetAmount.text = GeneralHelper.currencyFormat(wishlist.amount!!)

            this.textCurrentAmount.typeface = FontsHelper.INTER.light(activity)
            this.textCurrentAmount.text = GeneralHelper.currencyFormat(this.currentBalance(wishlist, daoSaving))

            GeneralHelper.setImageImpianCategory(context, wishlist.category!!, this.imageThumbnail)

            val percentage = this.calculatePercentage(wishlist, daoSaving ).toInt()

            if (percentage > 100){
                this.progressBar.setProgress(100, true)
                this.textCurrentProgress.text = "100%"
            }else{
                this.progressBar.setProgress(this.calculatePercentage(wishlist, daoSaving).toInt(), true)
                this.textCurrentProgress.text = this.progressBar.progress.toString() + "%"
            }

            if (wishlist.category == context.getString(R.string.wedding)){
                this.cardThumbnail.setCardBackgroundColor(context.getColor(R.color.pink_wedding))
                this.imageThumbnail.setColorFilter(ContextCompat.getColor(context, R.color.red_300), android.graphics.PorterDuff.Mode.SRC_IN);
            }


            this.textCurrentProgress.typeface = FontsHelper.INTER.medium(activity)

            if (wishlist.isCompleted!!){
                this.imageIndicator.visibility = View.VISIBLE
            }

        }

        private fun currentBalance(
            wishlist: EntityWishlist,
            daoSaving: DaoSaving
        ): Long {
            val saving = daoSaving.getAllByWishlistID(wishlist.id!!)
            var currentAmount = 0L
            for (i in saving){
                currentAmount += i.amount!!
            }

            return currentAmount
        }
         private fun calculatePercentage(
            wishlist: EntityWishlist,
            daoSaving: DaoSaving
        ): Float {
            val targetAmount = wishlist.amount.toString().toFloat()
            return ((this.currentBalance(wishlist, daoSaving).div(targetAmount)) * 100)
        }
    }
}