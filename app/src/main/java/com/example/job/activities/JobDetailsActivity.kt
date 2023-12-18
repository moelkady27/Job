package com.example.job.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.AddBookmarkResponse
import com.example.job.model.GetAllJobsResponse
import com.example.job.model.Job
import com.example.job.model.JobsResponse
import com.example.job.request.AddBookmarkRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_job_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailsActivity : AppCompatActivity() {

    private var jobId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        setupActionBar()

        val token = AppReference.getToken(this@JobDetailsActivity)
        RetrofitClient.instance.getAllJobs("Bearer $token")
            .enqueue(object : Callback<GetAllJobsResponse>{
                override fun onResponse(
                    call: Call<GetAllJobsResponse>,
                    response: Response<GetAllJobsResponse>
                ) {
                    if (response.isSuccessful) {

                        val jobList = response.body()?.jobs ?: emptyList()

                        val selectedPosition = intent.getIntExtra("selectedPosition", 0)
                        if (selectedPosition >= 0 && selectedPosition < jobList.size) {
                            val selectedJob = jobList[selectedPosition]

                            jobId = selectedJob._id

                            Glide.with(this@JobDetailsActivity)
                                .load(selectedJob.imageUrl)
                                .into(image_view)

                            tv_company.text = selectedJob.company
                            titleJob.text = selectedJob.title
                            locationJob.text = selectedJob.location
                            button_period.text = selectedJob.workHours
                            salaryJob.text = selectedJob.salary
                            jobDescription.text = selectedJob.description
                            jobRequirements.text = selectedJob.requirements.toString()

                        } else {
                            Toast.makeText(
                                this@JobDetailsActivity,
                                "Invalid position",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }

                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@JobDetailsActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllJobsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@JobDetailsActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })


        toolbar_menu_bookmark.setOnClickListener {
            val token = AppReference.getToken(this@JobDetailsActivity)
            RetrofitClient.instance.addBookmark("Bearer $token" , AddBookmarkRequest(jobId!!))
                .enqueue(object : Callback<AddBookmarkResponse>{
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(
                        call: Call<AddBookmarkResponse>,
                        response: Response<AddBookmarkResponse>
                    ) {
                        if (response.isSuccessful) {
                            val addBookmarkResponse = response.body()?.message
                                Toast.makeText(
                                    this@JobDetailsActivity,
                                    addBookmarkResponse,
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@JobDetailsActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AddBookmarkResponse>, t: Throwable) {
                        Toast.makeText(
                            this@JobDetailsActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_title)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar_title.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}