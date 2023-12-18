package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.DeleteUserResponse
import com.example.job.service.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_delete.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        setupActionBar()

        del.setOnClickListener {
            val passwordEditText = findViewById<TextInputEditText>(R.id.edt_pass)
            val password = passwordEditText.text.toString()
            val token = AppReference.getToken(this)
            val userId = AppReference.getUserId(this)

            deleteUser(userId, password, token)
        }

    }

    private fun deleteUser(userId: String, password: String, token: String) {
        RetrofitClient.instance.deleteUser("Bearer $token", userId , password)
            .enqueue(object : Callback<DeleteUserResponse>{
                override fun onResponse(
                    call: Call<DeleteUserResponse>,
                    response: Response<DeleteUserResponse>
                ) {
                    if (response.isSuccessful) {
                        val deleteResponse = response.body()
                        if (deleteResponse != null && deleteResponse.status) {
                            val message = deleteResponse.message
                            Toast.makeText(
                                    this@DeleteActivity,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            AppReference.setLoginState(this@DeleteActivity, false)
                            startActivity(Intent(this@DeleteActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                    this@DeleteActivity,
                                    "Invalid response from server",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@DeleteActivity,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {
                        Toast.makeText(
                            this@DeleteActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_delete)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar_delete.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}