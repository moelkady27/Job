package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        navDrawer01.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawerLayout01)

        imageView003.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
        Glide.with(this)
            .load(imageUrl)
            .into(imageView002)

        val isAdmin = AppReference.getIsAdmin(this@SettingActivity)

        if (isAdmin) {
            delete_acc.visibility = View.GONE
            textView33.visibility = View.GONE
            imageView6.visibility = View.GONE
            change_your.text = "Admin Panel"
            textView32.text = "manage your app"
            imageView5.setImageResource(R.drawable.baseline_manage_accounts_24)

            change_your.setOnClickListener {
                startActivity(Intent(this@SettingActivity , AdminPanelActivity::class.java))
            }

            imageView5.setOnClickListener {
                startActivity(Intent(this@SettingActivity , AdminPanelActivity::class.java))
            }

        } else {
            delete_acc.setOnClickListener {
                startActivity(Intent(this@SettingActivity, DeleteActivity::class.java))
            }

            imageView5.setOnClickListener {
                startActivity(Intent(this@SettingActivity , AdminPanelActivity::class.java))
            }

            change_your.setOnClickListener {
                startActivity(Intent(this@SettingActivity , ChangePasswordActivity::class.java))
            }
        }

        imageView8.setOnClickListener {
            AppReference.setLoginState(this@SettingActivity, false)
            startActivity(Intent(this@SettingActivity, MainActivity::class.java))
            finish()
        }

        logout.setOnClickListener {
            AppReference.setLoginState(this@SettingActivity, false)
            startActivity(Intent(this@SettingActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_setting -> {
                Toast.makeText(this@SettingActivity , "Setting" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_chat -> {
                startActivity(Intent(this@SettingActivity , ChatActivity::class.java))
                Toast.makeText(this@SettingActivity , "Chat" , Toast.LENGTH_LONG).show()
            }
            R.id.nav_home -> {
                Toast.makeText(this@SettingActivity , "Home" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@SettingActivity , JobsActivity::class.java))
            }
            R.id.nav_bookmarks -> {
                Toast.makeText(this@SettingActivity , "Bookmarks" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@SettingActivity , BookmarksActivity::class.java))
            }
            R.id.nav_profile -> {
                Toast.makeText(this@SettingActivity , "Profile" , Toast.LENGTH_LONG).show()
                startActivity(Intent(this@SettingActivity , ProfileActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}