package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job.AppReference
import com.example.job.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewNext1.setOnClickListener {
            startActivity(Intent(this , MainActivity2::class.java))
        }

        textViewSkip1.setOnClickListener {
            startActivity(Intent(this , MainActivity3::class.java))
        }

//                    ..................auto login................
        if (AppReference.getLoginState(this@MainActivity)){
            startActivity(Intent(this@MainActivity , JobsActivity::class.java))
            finish()
        }
    }
}