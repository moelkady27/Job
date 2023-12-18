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
import com.example.job.adapter.ChatItemsAdapter
import com.example.job.model.GetAllChatsResponse
import com.example.job.service.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        drawerLayout = findViewById(R.id.drawer_chat)

        navDrawerChat.setNavigationItemSelectedListener(this@ChatActivity)

        imageView06.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(imageView05)

        recyclerView = findViewById(R.id.recyclerviewChat)
        adapter = ChatItemsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = adapter

        fetchChats()

    }

    private fun fetchChats() {
        val token = AppReference.getToken(this)
        RetrofitClient.instance.getAllChats("Bearer $token")
            .enqueue(object : Callback<GetAllChatsResponse>{
                override fun onResponse(
                    call: Call<GetAllChatsResponse>,
                    response: Response<GetAllChatsResponse>
                ) {
                    if (response.isSuccessful) {
                        val chatsResponse = response.body()
                        if (chatsResponse?.status == true) {
                            val chats = chatsResponse.chats
                            adapter = ChatItemsAdapter(chats)
                            recyclerView.adapter = adapter
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@ChatActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@ChatActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllChatsResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ChatActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                startActivity(Intent(this@ChatActivity , JobsActivity::class.java))
                Toast.makeText(this@ChatActivity , "There is Your Home" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_chat -> {
                Toast.makeText(this@ChatActivity , "Chat" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this@ChatActivity , "Setting" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ChatActivity , SettingActivity::class.java))
            }
            R.id.nav_bookmarks -> {
                Toast.makeText(this@ChatActivity , "Bookmarks" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ChatActivity , BookmarksActivity::class.java))
            }
            R.id.nav_profile -> {
                Toast.makeText(this@ChatActivity , "Profile" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ChatActivity , ProfileActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}