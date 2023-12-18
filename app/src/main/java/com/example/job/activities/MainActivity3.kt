package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job.R
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        button_register.setOnClickListener {
            startActivity(Intent(this , RegisterActivity::class.java))
        }

        button_login.setOnClickListener {
            startActivity(Intent(this , LoginActivity::class.java))
        }
    }
}