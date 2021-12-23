package com.steadytech.impian.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.steadytech.impian.R
import com.steadytech.impian.activity.MainActivity
import com.steadytech.impian.database.AppDatabase
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.helper.*
import java.text.SimpleDateFormat

class SavingAdapter(
    private val datas: List<EntitySaving>,
    private val activity: Activity,
    private val database : AppDatabase = DatabaseHelper.localDb(activity)
) : RecyclerView.Adapter<SavingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_saving, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return this.datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], activity, database)



        holder.itemView.setOnClickListener {
            val wishlist = this.database.daoWishlist().get(datas[position].id!!)
            BottomSheets.information(
                datas[position],
                wishlist.isCompleted!!,
                activity as AppCompatActivity,
                View.OnClickListener {
                    BottomSheets.confirmation(
                        activity.getString(R.string.ask_delete_saving_history),
                        activity.getString(R.string.subtitle_delete_saving_history),
                        activity.getString(R.string.yes_continue),
                        Constant.TAG.DetailGoalsActivity,
                        activity,
                        View.OnClickListener {

                            Toast.makeText(activity, "Berhasil Menghapus Data!", Toast.LENGTH_SHORT)
                                .show()
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                            activity.finish()
                        }
                    )
                }
            )
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var textAmount: TextView
        private lateinit var textImpianName: TextView
        private lateinit var textDate: TextView

        fun bind(saving: EntitySaving, activity: Activity, database: AppDatabase) {
            this.textAmount = this.itemView.findViewById(R.id.textAmount)
            this.textImpianName = this.itemView.findViewById(R.id.textImpianName)

            this.textImpianName.text = database.daoWishlist().getById(saving.wishlistID!!).name
            this.textAmount.text = GeneralHelper.currencyFormat(saving.amount!!)

            var sdf = SimpleDateFormat("yyyymmdd")
            val date = sdf.parse(saving.savingDate)

            sdf = SimpleDateFormat("EEEE, dd/MM/yyyy")

            this.textDate = this.itemView.findViewById(R.id.textDate)
            this.textDate.typeface = FontsHelper.INTER.light(activity)
            this.textDate.text = sdf.format(date)

            FontsHelper.INTER.medium(activity, this.textAmount, this.textImpianName)
        }
    }
}