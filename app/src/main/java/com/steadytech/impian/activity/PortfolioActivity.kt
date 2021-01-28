 package com.steadytech.impian.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steadytech.impian.R
import com.steadytech.impian.helper.FontsHelper
import com.steadytech.impian.helper.GeneralHelper
import com.steadytech.impian.model.realm.Wishlist
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.io.*
import java.lang.Exception
import java.util.*

class PortfolioActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var textTitleBalance: TextView
    private lateinit var textBalance: TextView
    private lateinit var textTitleAchieved: TextView
    private lateinit var textAchieved: TextView
    private lateinit var textBack: TextView
    private lateinit var textHashtag: TextView
    private lateinit var textDescription: TextView
    private lateinit var textStory: TextView

    private lateinit var cardBackground: CardView

    private lateinit var inputDescription: EditText

    private lateinit var fabShare: FloatingActionButton

    private lateinit var linearBack: LinearLayout

    private lateinit var wishlist: RealmResults<Wishlist>
    private lateinit var realm: Realm

    private var currentBalance: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_portfolio)
        this.supportActionBar!!.hide()

        this.extract()

        this.init()

        this.inputDescription.addTextChangedListener(this)

        this.linearBack.setOnClickListener(this)
        this.fabShare.setOnClickListener(this)
        this.textStory.setOnClickListener(this)
    }

    private fun extract() {
        this.currentBalance = this.intent.getLongExtra("currentBalance", 0L).toLong()
    }

    private fun init() {
        this.realm = Realm.getDefaultInstance()
        this.wishlist = this.realm.where<Wishlist>().equalTo("isCompleted", true).findAll()

        this.textTitleBalance = findViewById(R.id.textTitleBalance)
        this.textTitleBalance.typeface = FontsHelper.INTER.regular(this)

        this.textBalance = findViewById(R.id.textBalance)
        this.textBalance.typeface = FontsHelper.INTER.regular(this)

        this.textTitleAchieved = findViewById(R.id.textTitleAchieved)
        this.textTitleAchieved.typeface = FontsHelper.INTER.regular(this)

        this.textAchieved = findViewById(R.id.textAchieved)
        this.textAchieved.typeface = FontsHelper.INTER.regular(this)

        this.textStory = findViewById(R.id.textStory)
        this.textStory.typeface = FontsHelper.INTER.regular(this)

        this.textBack = findViewById(R.id.textBack)
        this.textBack.typeface = FontsHelper.INTER.regular(this)

        this.linearBack = findViewById(R.id.linearBack)

        this.inputDescription = findViewById(R.id.inputDescription)
        this.inputDescription.typeface = FontsHelper.INTER.regular(this)

        this.textBalance.text = GeneralHelper.currencyFormat(this.currentBalance)
        this.textAchieved.text = this.wishlist.size.toString() + " Impian Tercapai"

        this.textDescription = findViewById(R.id.textDescription)
        this.textDescription.typeface = FontsHelper.INTER.medium(this)

        this.textHashtag = findViewById(R.id.textHashtag)
        this.textHashtag.typeface = FontsHelper.INTER.medium(this)

        this.cardBackground = findViewById(R.id.cardBackground)

        this.fabShare = findViewById(R.id.fabShare)
    }

    override fun onClick(v: View?) {
        if (v == this.linearBack) {
            super.onBackPressed()
        } else if (v == this.fabShare) {
            try {
                this.share()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else if (v == this.textStory){
            this.textStory.visibility = View.GONE
            this.inputDescription.visibility = View.VISIBLE
        }
    }

    private fun share() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Allow the Storage Permission", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 401)
        } else {
            this.cardBackground.isDrawingCacheEnabled = true
            this.cardBackground.setCardBackgroundColor(this.resources.getColor(android.R.color.darker_gray))
            val b: Bitmap = cardBackground.getDrawingCache()
            val random = Random().nextInt(100000 - 100 + 1) + 100000
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val location = "/IMG-SHARE-$random-IMPIAN.png"
            val path = root.toString() + location
            val imageDir = File(root, location)
            try {
                b.compress(Bitmap.CompressFormat.PNG, 95, FileOutputStream(path))
                Log.d("SUCCESS SHARE IMAGE", "SUCCESS==============")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.d("FAIL SHARE IMAGE", "ERROR==============")
            }
            val finalPath = FileInputStream(File(path))
            finalPath.close()
            val i = Intent()
            i.action = Intent.ACTION_SEND
            i.type = "image/png"
//            i.putExtra(Intent.EXTRA_TEXT, shareMessages)
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageDir.absolutePath))
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(i, "Share Your Transaction"))
            this.cardBackground.setCardBackgroundColor(this.resources.getColor(R.color.white))
        }
    }

    override fun afterTextChanged(s: Editable?) {
        this.textStory.text = s.toString()
        this.textStory.visibility = View.VISIBLE
        this.inputDescription.visibility = View.GONE
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}