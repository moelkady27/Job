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
import com.example.job.adapter.BookmarksAdapter
import com.example.job.model.GetAllBookmarksResponse
import com.example.job.service.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_bookmarks.*
import kotlinx.android.synthetic.main.activity_jobs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarksActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        navDrawer1.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawerLayout1)

        imageView03.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(imageView02)

        recyclerView = findViewById(R.id.recyclerview01)
        adapter = BookmarksAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = adapter

        fetchBookmarks()
    }

    private fun fetchBookmarks() {
        val token = AppReference.getToken(this)
        RetrofitClient.instance.getAllBookmarks("Bearer $token")
            .enqueue(object : Callback<GetAllBookmarksResponse>{
                override fun onResponse(
                    call: Call<GetAllBookmarksResponse>,
                    response: Response<GetAllBookmarksResponse>
                ) {
                    if (response.isSuccessful) {
                        val bookmarksResponse = response.body()
                        if (bookmarksResponse?.status == true) {
                            val bookmarks = bookmarksResponse.bookmarks
                            adapter = BookmarksAdapter(bookmarks)
                            recyclerView.adapter = adapter
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@BookmarksActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@BookmarksActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllBookmarksResponse>, t: Throwable) {
                    Toast.makeText(
                        this@BookmarksActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                startActivity(Intent(this@BookmarksActivity , JobsActivity::class.java))
                Toast.makeText(this@BookmarksActivity , "There is Your Home" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_chat -> {
                startActivity(Intent(this@BookmarksActivity , ChatActivity::class.java))
                Toast.makeText(this@BookmarksActivity , "Chat" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this@BookmarksActivity , "Setting" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@BookmarksActivity , SettingActivity::class.java))
            }
            R.id.nav_profile -> {
                Toast.makeText(this@BookmarksActivity , "Profile" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@BookmarksActivity , ProfileActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}