package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.UpdateResponse
import com.example.job.request.UpdateRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        update.setOnClickListener {

            val phone = edt_phone1.text.toString().trim()
            val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"

            val location = edt_location.text.toString().trim()

            val secondSkill1 = edt_second_skill1.text.toString().trim()
            val thirdSkill1 = edt_third_skill1.text.toString().trim()
            val fourthSkill1 = edt_fourth_skill1.text.toString().trim()
            val fifthSkill1 = edt_fifth_skill1.text.toString().trim()

            val skills = listOf(location, secondSkill1, thirdSkill1, fourthSkill1, fifthSkill1)

            val data = UpdateRequest(location, phone, imageUrl, skills)

            val token = AppReference.getToken(this@UpdateUserActivity)

            RetrofitClient.instance.updateUser("Bearer $token" , data)
                .enqueue(object : Callback<UpdateResponse>{
                    override fun onResponse(
                        call: Call<UpdateResponse>,
                        response: Response<UpdateResponse>
                    ) {
                        if (response.isSuccessful){
                            val defaultResponse = response.body()
                            if (defaultResponse != null){
                                val message = defaultResponse.status
                                Toast.makeText(
                                    this@UpdateUserActivity,
                                    "$message",
                                    Toast.LENGTH_LONG
                                ).show()
                                AppReference.setLoginState(this@UpdateUserActivity, false)
                                startActivity(Intent(this@UpdateUserActivity, LoginActivity::class.java))
                                finish()
                                Log.e("Data = " , "$location $phone $imageUrl $skills")
                            } else {
                                Toast.makeText(
                                    this@UpdateUserActivity,
                                    "Invalid response from server",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@UpdateUserActivity,
                                "$error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                        Toast.makeText(
                            this@UpdateUserActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }
}