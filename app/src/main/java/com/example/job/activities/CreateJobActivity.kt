package com.example.job.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.CreateJobResponse
import com.example.job.model.GetUserProfileResponse
import com.example.job.model.RegisterResponse
import com.example.job.request.CreateJobRequest
import com.example.job.request.SignUpRequest
import com.example.job.service.RetrofitClient
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_admin_panel.*
import kotlinx.android.synthetic.main.activity_complete_account.*
import kotlinx.android.synthetic.main.activity_create_job.*
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateJobActivity : AppCompatActivity() {

    private val PICK_IMAGE = 1

    private lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)

        setupActionBar()

        storageReference = FirebaseStorage.getInstance().reference


//        val imageUrl = "https://pbs.twimg.com/media/BtFUrp6CEAEmsml?format=jpg&name=small"
//        Glide.with(this)
//            .load(imageUrl)
//            .into(imageView10)

        imageView10.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        createJob.setOnClickListener {

            uploadImageToFirebaseStorage()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageView10.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        val currentTimeMillis = System.currentTimeMillis()
        val imageFileName = "image_$currentTimeMillis.jpg"

        val imageRef = storageReference.child("images/$imageFileName")

        imageView10.isDrawingCacheEnabled = true
        imageView10.buildDrawingCache()
        val bitmap = imageView10.drawingCache

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val uploadTask = imageRef.putBytes(imageData)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val imageUrl = downloadUri.toString()
                    println("Image URL: $imageUrl")

                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                    AppReference.setImageJob(this, imageUrl)


                    val title = edtCreateJobTitle.text.toString().trim()
                    val location = edtCreateJobLocation.text.toString().trim()
                    val company = edtCreateJobCompanyName.text.toString().trim()
                    val description = edtCreateJobDescription.text.toString().trim()
                    val salary = edtCreateJobSalary.text.toString().trim()
                    val workHours = edtCreateJobWorkHours.text.toString().trim()
                    val contractPeriod = edtCreateJobContractPeriod.text.toString().trim()
                    val image = AppReference.getImageJob(this)

                    val firstRequire = edtCreateFirstRequirement.text.toString().trim()
                    val secondRequire = edtCreateSecondRequirement.text.toString().trim()
                    val thirdRequire = edtCreateThirdRequirement.text.toString().trim()
                    val fourthRequire = edtCreateFourthRequirement.text.toString().trim()

                    val requirements = listOf(firstRequire, secondRequire, thirdRequire, fourthRequire)

                    val data = CreateJobRequest(title, location, company, description, salary, workHours, contractPeriod, image, requirements)
                    val token = AppReference.getToken(this)

                    RetrofitClient.instance.createJob("Bearer $token", data)
                        .enqueue(object : Callback<CreateJobResponse>{
                            override fun onResponse(
                                call: Call<CreateJobResponse>,
                                response: Response<CreateJobResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val createJobResponse = response.body()
                                    val message = createJobResponse?.message
                                    if (createJobResponse != null && createJobResponse.status) {
                                        Toast.makeText(
                                            this@CreateJobActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@CreateJobActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    val error = response.errorBody()?.string()
                                    Toast.makeText(
                                        this@CreateJobActivity,
                                        "$error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e("error is ", "$error")
                                }
                            }

                            override fun onFailure(call: Call<CreateJobResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@CreateJobActivity,
                                    "Network error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(
                this,
                "Image upload failed: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_job)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbar_create_job.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}