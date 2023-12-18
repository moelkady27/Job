package com.example.job.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job.AppReference
import com.example.job.R
import com.example.job.adapter.JobAdapter
import com.example.job.adapter.SearchAdapter
import com.example.job.model.SearchResponse
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_job_details.*
import kotlinx.android.synthetic.main.activity_jobs.*
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupActionBar()

        recyclerView = findViewById(R.id.rec)
        adapter = SearchAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchForJobs(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    private fun setupActionBar() {
        setSupportActionBar(tool)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_color_white_24)
        }

        tool.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun searchForJobs(query: String) {
        val token = AppReference.getToken(this@SearchActivity)
        RetrofitClient.instance.searchJobs("Bearer $token", query)
            .enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val searchResults = response.body()?.searchResults ?: emptyList()

                    adapter = SearchAdapter(searchResults)
                    recyclerView.adapter = adapter
                }
                else{
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@SearchActivity,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()            }
        })
    }
}