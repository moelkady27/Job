package com.example.job.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.CreateJobResponse
import com.example.job.model.UpdateJobResponse
import com.example.job.request.CreateJobRequest
import com.example.job.request.UpdateJobRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_job.*
import kotlinx.android.synthetic.main.activity_update_job.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_job)

        setupActionBar()

        val jobTitle = intent.getStringExtra("title")
        val companyName = intent.getStringExtra("company")
        val jobDescription = intent.getStringExtra("description")
        val jobLocation = intent.getStringExtra("location")
        val jobSalary = intent.getStringExtra("salary")
        val contractPeriod = intent.getStringExtra("contractPeriod")
        val workHours = intent.getStringExtra("workHours")
        val imageUrl = intent.getStringExtra("imageUrl")
        val requirements = intent.getStringArrayListExtra("requirements")

        if (requirements != null && requirements.size >= 4){
            val firstRequirement = requirements[0]
            val secondRequirement = requirements[1]
            val thirdRequirement = requirements[2]
            val fourthRequirement = requirements[3]

            edtCreateFirstRequirement1.setText(firstRequirement)
            edtCreateSecondRequirement1.setText(secondRequirement)
            edtCreateThirdRequirement1.setText(thirdRequirement)
            edtCreateFourthRequirement1.setText(fourthRequirement)
        }

        edtCreateJobTitle1.setText(jobTitle)
        edtCreateJobCompanyName1.setText(companyName)
        edtCreateJobDescription1.setText(jobDescription)
        edtCreateJobLocation1.setText(jobLocation)
        edtCreateJobSalary1.setText(jobSalary)
        edtCreateJobContractPeriod1.setText(contractPeriod)
        edtCreateJobWorkHours1.setText(workHours)
        Glide.with(this)
            .load(imageUrl)
            .into(imageView101)

        createJob1.setOnClickListener {

            val id = intent.getStringExtra("id").toString()
            val title = edtCreateJobTitle1.text.toString().trim()
            val location = edtCreateJobLocation1.text.toString().trim()
            val company = edtCreateJobCompanyName1.text.toString().trim()
            val description = edtCreateJobDescription1.text.toString().trim()
            val salary = edtCreateJobSalary1.text.toString().trim()
            val workHours = edtCreateJobWorkHours1.text.toString().trim()
            val contractPeriod = edtCreateJobContractPeriod1.text.toString().trim()
            val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"

            val firstRequire = edtCreateFirstRequirement1.text.toString().trim()
            val secondRequire = edtCreateSecondRequirement1.text.toString().trim()
            val thirdRequire = edtCreateThirdRequirement1.text.toString().trim()
            val fourthRequire = edtCreateFourthRequirement1.text.toString().trim()

            val requirements = listOf(firstRequire, secondRequire, thirdRequire, fourthRequire)

            val data = UpdateJobRequest(id , title, location, company, description, salary, workHours, contractPeriod, imageUrl, requirements)
            val token = AppReference.getToken(this)

            RetrofitClient.instance.updateJob("Bearer $token", data)
                .enqueue(object : Callback<UpdateJobResponse>{
                    override fun onResponse(
                        call: Call<UpdateJobResponse>,
                        response: Response<UpdateJobResponse>
                    ) {
                        if (response.isSuccessful) {
                            val createJobResponse = response.body()
                            val message = createJobResponse?.status.toString()
                            if (createJobResponse != null && createJobResponse.status) {
                                Toast.makeText(
                                    this@UpdateJobActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@UpdateJobActivity , JobsActivity::class.java))

                                finish()


                            } else {
                                Toast.makeText(
                                    this@UpdateJobActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(
                                this@UpdateJobActivity,
                                "$error",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("error is ", "$error")
                        }
                    }

                    override fun onFailure(call: Call<UpdateJobResponse>, t: Throwable) {
                        Toast.makeText(
                            this@UpdateJobActivity,
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_update_job)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar_update_job.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}