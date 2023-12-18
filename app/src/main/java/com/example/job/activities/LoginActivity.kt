package com.example.job.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.LoginResponse
import com.example.job.request.LoginRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textView13.setOnClickListener {
            startActivity(Intent(this , RegisterActivity::class.java))
        }

        login.setOnClickListener {
            val email = edt_email_login.text.toString().trim()
            val password = edt_password_login.text.toString().trim()

            val data = LoginRequest(email, password)
            RetrofitClient.instance.login(data)
                .enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse!=null){
                                val message = loginResponse.status
                                val token = loginResponse.token
                                val userId = loginResponse.userData._id
                                val isAdmin = loginResponse.userData.isAdmin
                                cacheToken(token)
                                cacheUserId(userId)
                                AppReference.setUserId(this@LoginActivity , userId)
                                AppReference.getUserId(this@LoginActivity)
                                AppReference.setToken(this@LoginActivity, token)
                                AppReference.setIsAdmin(this@LoginActivity, isAdmin)
                                Log.d("token is " , token)
                                Log.e("user id id " , userId)
                                Toast.makeText(
                                    applicationContext,
                                    "$message",
                                    Toast.LENGTH_LONG
                                ).show()

                                if (isAdmin) {
                                    Toast.makeText(
                                        applicationContext,
                                        "You are an admin!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                Log.d("data = " , "$email \n $password \n $token")
                                startActivity(Intent(this@LoginActivity, JobsActivity::class.java))
                                finish()
                            } else{
                                Toast.makeText(
                                    applicationContext,
                                    "Invalid response from server",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else{
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@LoginActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    fun cacheToken(token: String?) {
        val sharedPrefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun cacheUserId(userId: String?) {
        val sharedPrefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("userId", userId)
        editor.apply()
    }
}