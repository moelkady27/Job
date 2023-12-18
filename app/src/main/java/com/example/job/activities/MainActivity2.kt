package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job.R
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textViewNext2.setOnClickListener {
            startActivity(Intent(this , MainActivity3::class.java))
        }

        textViewSkip2.setOnClickListener {
            startActivity(Intent(this , MainActivity3::class.java))
        }
    }
}