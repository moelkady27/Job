package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.job.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textView9.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        complete_register.setOnClickListener {

            val intent = Intent(this@RegisterActivity , CompleteAccountActivity::class.java)
            intent.putExtra("username" , edt_name.text.toString())
            intent.putExtra("email" , edt_email.text.toString())
            intent.putExtra("password" , edt_password.text.toString())
            startActivity(intent)
        }
    }
}