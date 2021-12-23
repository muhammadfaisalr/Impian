package com.steadytech.impian.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.switchmaterial.SwitchMaterial
import com.steadytech.impian.R
import com.steadytech.impian.helper.*

class SettingActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var textBack: TextView
    private lateinit var textMigrate: TextView
    private lateinit var textOfflineMode: TextView

    private lateinit var switchFreeMode : SwitchMaterial

    private lateinit var imageInfo: ImageView
    private lateinit var imageInfoMigrate: ImageView

    private lateinit var relativeMigrate : RelativeLayout

    private lateinit var linearBack: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_setting)
        this.supportActionBar!!.hide()

        this.init()

        this.imageInfo.setOnClickListener(this)
        this.imageInfoMigrate.setOnClickListener(this)
        this.linearBack.setOnClickListener(this)
        this.switchFreeMode.setOnCheckedChangeListener(this)
    }

    private fun init() {
        this.textBack = findViewById(R.id.textBack)
        this.textBack.typeface = FontsHelper.INTER.medium(this)

        this.textOfflineMode = findViewById(R.id.textModeOffline)
        this.textOfflineMode.typeface = FontsHelper.INTER.regular(this)

        this.textMigrate = findViewById(R.id.textMigrate)
        this.textMigrate.typeface = FontsHelper.INTER.regular(this)

        this.switchFreeMode = findViewById(R.id.switchFreeMode)

        this.imageInfo = findViewById(R.id.imageInfo)
        this.imageInfoMigrate = findViewById(R.id.imageInfoMigrate)

        this.relativeMigrate = findViewById(R.id.relativeMigrate)

        if (!UserHelper.isAnonymous(this)){
            this.relativeMigrate.visibility = View.GONE
        }

        val isChecked = SharedPreferenceHelper.get(this, Constant.KEY.MODE, Boolean::class.java)

        this.switchFreeMode.isChecked = isChecked as Boolean

        this.linearBack = findViewById(R.id.linearBack)

    }

    override fun onClick(v: View?) {
          when (v) {
            this.linearBack -> {
                this.finish()
            }

            this.imageInfo -> {
                BottomSheets.description(
                    R.drawable.ic_free,
                    this.getString(R.string.offline_mode),
                    this.getString(R.string.description_offline_mode),
                    this,
                    SettingActivity::class.java.simpleName
                )
            }

            this.imageInfoMigrate -> {
                BottomSheets.description(
                    R.drawable.ic_computer_24,
                    this.getString(R.string.migrate),
                    this.getString(R.string.description_migrate),
                    this,
                    SettingActivity::class.java.simpleName
                )
            }

        }
    }

    private fun saveMode(isChecked: Boolean) {
        Toast.makeText(this, isChecked.toString(), Toast.LENGTH_SHORT).show()
        SharedPreferenceHelper.save(this, Constant.KEY.MODE, isChecked)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        this.saveMode(isChecked)
    }
}