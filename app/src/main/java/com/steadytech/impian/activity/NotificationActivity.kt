package com.steadytech.impian.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.steadytech.impian.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityNotificationBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)
        this.supportActionBar?.hide()

        this.binding.apply {

        }
    }
}