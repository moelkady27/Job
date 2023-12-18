package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.job.R
import kotlinx.android.synthetic.main.activity_admin_panel.*
import kotlinx.android.synthetic.main.activity_job_details.*
import kotlinx.android.synthetic.main.activity_setting.*

class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        setupActionBar()

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(toolbar_image_admin)

        imageView005.setOnClickListener {
            startActivity(Intent(this@AdminPanelActivity , CreateJobActivity::class.java))
        }

        create_job.setOnClickListener {
            startActivity(Intent(this@AdminPanelActivity , CreateJobActivity::class.java))
        }

        app_jobs.setOnClickListener {
            startActivity(Intent(this@AdminPanelActivity , ViewAllJobsActivity::class.java))
        }

        imageView006.setOnClickListener {
            startActivity(Intent(this@AdminPanelActivity , ViewAllJobsActivity::class.java))
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_admin)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar_admin.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}