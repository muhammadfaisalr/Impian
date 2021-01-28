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
import com.steadytech.impian.helper.BottomSheets
import com.steadytech.impian.helper.Constant
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Saving
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*

class SavingAdapter(
    private val datas: RealmResults<Saving>,
    private val activity: Activity,
    private var realm: Realm = Realm.getDefaultInstance()
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
        holder.bind(datas[position], activity)



        holder.itemView.setOnClickListener {
            val wishlist =
                this.realm.where<Wishlist>().equalTo("id", datas[position]!!.targetId).findFirst()
            BottomSheets.information(
                datas[position]!!,
                wishlist!!.isCompleted,
                activity as AppCompatActivity,
                View.OnClickListener {
                    BottomSheets.confirmation(
                        activity.getString(R.string.ask_delete_saving_history),
                        activity.getString(R.string.subtitle_delete_saving_history),
                        activity.getString(R.string.yes_continue),
                        Constant.TAG.DetailGoalsActivity,
                        activity,
                        View.OnClickListener {
                            this.realm.executeTransaction{
                                datas[position]!!.deleteFromRealm()
                            }
                            Toast.makeText(activity, "Berhasil Menghapus Data!", Toast.LENGTH_SHORT).show()
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
        private lateinit var textDate: TextView

        fun bind(saving: Saving?, activity: Activity) {
            this.textAmount = this.itemView.findViewById(R.id.textAmount)
            this.textAmount.typeface = FontsHelper.INTER.medium(activity)
            this.textAmount.text = GeneralHelper.currencyFormat(saving!!.amount)

            var sdf = SimpleDateFormat("yyyymmdd")
            val date = sdf.parse(saving!!.savingDate)

            sdf = SimpleDateFormat("EEEE, dd/MM/yyyy")

            this.textDate = this.itemView.findViewById(R.id.textDate)
            this.textDate.typeface = FontsHelper.INTER.light(activity)
            this.textDate.text = sdf.format(date)
        }
    }
}