package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.ChangeResponse
import com.example.job.request.ChangeRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        confirm.setOnClickListener {
            val currentPassword = edt_current.text.toString().trim()
            val newPassword = edt_new.text.toString().trim()

            val token = AppReference.getToken(this@ChangePasswordActivity)

            val data = ChangeRequest(currentPassword, newPassword)
            RetrofitClient.instance.changePassword("Bearer $token" , data)
                .enqueue(object : Callback<ChangeResponse> {
                    override fun onResponse(
                        call: Call<ChangeResponse>,
                        response: Response<ChangeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val changeResponse = response.body()
                            if (changeResponse!=null){
                                val message = changeResponse.status
                                AppReference.setLoginState(this@ChangePasswordActivity, true)
                                Toast.makeText(
                                    applicationContext,
                                    "$message",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("current is " , currentPassword)
                                Log.d("new is " , newPassword)
                                startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
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
                                this@ChangePasswordActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ChangeResponse>, t: Throwable) {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }
}