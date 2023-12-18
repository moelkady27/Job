package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.adapter.JobAdapter
import com.example.job.model.GetAllJobsResponse
import com.example.job.service.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_jobs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobAdapter

//    **************** double back to exit ***************
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) {
            super.onBackPressed()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
//            finish()
        }
    }
//     **************** double back to exit ***************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        navDrawer.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawerLayout)

        imageView3.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        textView22.setOnClickListener {
            startActivity(Intent(this@JobsActivity , ViewAllJobsActivity::class.java))
        }

        searchView.setOnClickListener {
            startActivity(Intent(this@JobsActivity , SearchActivity::class.java))
        }

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(imageView2)

        recyclerView = findViewById(R.id.recyclerview)
        adapter = JobAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        recyclerView.adapter = adapter

        getJobs()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_chat -> {
                startActivity(Intent(this@JobsActivity , ChatActivity::class.java))
                Toast.makeText(this@JobsActivity , "Chat" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_bookmarks -> {
                startActivity(Intent(this@JobsActivity , BookmarksActivity::class.java))
                Toast.makeText(this@JobsActivity , "There is Your Bookmarks" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this@JobsActivity , "Setting" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@JobsActivity , SettingActivity::class.java))
            }
            R.id.nav_profile -> {
                Toast.makeText(this@JobsActivity , "Profile" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@JobsActivity , ProfileActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getJobs(){
        val token = AppReference.getToken(this@JobsActivity)
        RetrofitClient.instance.getAllJobs("Bearer $token")
            .enqueue(object : Callback<GetAllJobsResponse>{
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

                        adapter = JobAdapter(jobList)
                        recyclerView.adapter = adapter
                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@JobsActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllJobsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@JobsActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }
}