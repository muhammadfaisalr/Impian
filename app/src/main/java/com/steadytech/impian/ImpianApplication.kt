package com.steadytech.impian

import android.app.Application
import com.steadytech.impian.database.entity.EntityImpianCategory
import com.steadytech.impian.helper.*
import io.realm.Realm
import org.w3c.dom.Entity

class ImpianApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        this.setCategories()
    }

    private fun setCategories() {
        val db =  DatabaseHelper.localDb(this)

        val categories = db.daoCategory().getAll() as ArrayList<EntityImpianCategory>

        if (categories.isEmpty()){
            categories.add(EntityImpianCategory(null, this.getString(R.string.wedding), R.drawable.ic_round_favorite_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.vacation), R.drawable.ic_baseline_airplane_ticket_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.electronic), R.drawable.ic_round_headphones_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.home), R.drawable.ic_round_maps_home_work_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.vehicle), R.drawable.ic_round_directions_car_filled_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.health), R.drawable.ic_round_local_hospital_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.education), R.drawable.ic_round_school_24))
            categories.add(EntityImpianCategory(null, this.getString(R.string.etc), R.drawable.ic_round_space_dashboard_24))

            for (entity in categories){
                db.daoCategory().insert(entity)
            }
        }
    }
}
