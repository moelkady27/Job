package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.GetUserProfileResponse
import com.example.job.service.RetrofitClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.imageView03
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        navDrawer2.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawerLayout2)

        imageView04.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(imageView03)

        getUserProfile()
    }

    private fun getUserProfile() {
        val token = AppReference.getToken(this@ProfileActivity)
        RetrofitClient.instance.getUser("Bearer $token")
            .enqueue(object : Callback<GetUserProfileResponse>{
                override fun onResponse(
                    call: Call<GetUserProfileResponse>,
                    response: Response<GetUserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val defaultResponse = response.body()
                        val userData = defaultResponse?.userData
                        val message = defaultResponse?.status

                        Glide.with(this@ProfileActivity)
                            .load(userData?.imageUrl)
                            .into(imageView4)

                        text_username.text = userData?.username
                        text_location.text = userData?.location
                        text_email.text = userData?.email
                        text_phone.text = userData?.phone
                        text_skills.text = userData?.skills.toString()

                        Toast.makeText(
                            this@ProfileActivity,
                            "$message",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@ProfileActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                startActivity(Intent(this@ProfileActivity , JobsActivity::class.java))
                Toast.makeText(this@ProfileActivity , "There is Your Home" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_chat -> {
                startActivity(Intent(this@ProfileActivity , ChatActivity::class.java))
                Toast.makeText(this@ProfileActivity , "Chat" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this@ProfileActivity , "Setting" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ProfileActivity , SettingActivity::class.java))
            }
            R.id.nav_bookmarks -> {
                Toast.makeText(this@ProfileActivity , "Bookmarks" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ProfileActivity , BookmarksActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}