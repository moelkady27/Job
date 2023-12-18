package com.example.job.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job.AppReference
import com.example.job.R
import com.example.job.adapter.ViewAllJobsAdapter
import com.example.job.model.GetAllJobsResponse
import com.example.job.model.JobsResponse
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_view_all_jobs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllJobsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewAllJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_jobs)

        setupActionBar()


        recyclerView = findViewById(R.id.recyclerview1)
        adapter = ViewAllJobsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = adapter

        getJobs()

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getJobs(){
        val token = AppReference.getToken(this@ViewAllJobsActivity)
        RetrofitClient.instance.getAllJobs("Bearer $token")
            .enqueue(object : Callback<GetAllJobsResponse> {
                override fun onResponse(
                    call: Call<GetAllJobsResponse>,
                    response: Response<GetAllJobsResponse>
                ) {
                    if (response.isSuccessful) {
                        val jobList = response.body()?.jobs ?: emptyList()

//                        for (job in jobList) {
//                            Log.e("company is ", job.company)
//                            Log.e("title is ", job.title)
//                            Log.e("location is ", job.location)
//                            Log.e("salary is ", job.salary)
//                        }

                        adapter = ViewAllJobsAdapter(jobList)
                        recyclerView.adapter = adapter
                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@ViewAllJobsActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllJobsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ViewAllJobsActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }
}