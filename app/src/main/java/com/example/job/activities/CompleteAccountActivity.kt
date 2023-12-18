package com.example.job.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.RegisterResponse
import com.example.job.request.SignUpRequest
import com.example.job.service.RetrofitClient
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_complete_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CompleteAccountActivity : AppCompatActivity() {

    private val PICK_IMAGE = 1

    private lateinit var storageReference: StorageReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_account)

        storageReference = FirebaseStorage.getInstance().reference


        imageButton0.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        create_account.setOnClickListener {

            uploadImageToFirebaseStorage()

//            val username = intent.getStringExtra("username").toString().trim()
//            val email = intent.getStringExtra("email").toString().trim()
//            val password = intent.getStringExtra("password").toString().trim()
//
//            val phone = edt_phone.text.toString().trim()
//
//            val firstSkill = edt_first_skill.text.toString().trim()
//            val secondSkill = edt_second_skill.text.toString().trim()
//            val thirdSkill = edt_third_skill.text.toString().trim()
//            val fourthSkill = edt_fourth_skill.text.toString().trim()
//            val fifthSkill = edt_fifth_skill.text.toString().trim()
//
//            val skills = listOf(firstSkill, secondSkill, thirdSkill, fourthSkill, fifthSkill)
//
//            val resumeUrl = null
//
//            val data = SignUpRequest(username, email, password, phone, skills, resumeUrl.toString())
//            RetrofitClient.instance.register(data)
//                .enqueue(object : Callback<RegisterResponse> {
//                    override fun onResponse(
//                        call: Call<RegisterResponse>,
//                        response: Response<RegisterResponse>
//                    ) {
//                        if (response.isSuccessful){
//                            val defaultResponse = response.body()
//                            if (defaultResponse != null){
//                                val message = defaultResponse.message
//                                Toast.makeText(
//                                    baseContext,
//                                    message,
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                startActivity(Intent(this@CompleteAccountActivity, LoginActivity::class.java))
//                                finish()
//                                Log.e("Data = " , "$username $email $password $phone $resumeUrl")
//                            } else {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Invalid response from server",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        } else {
//                            val error = response.errorBody()?.string()
//                            Toast.makeText(
//                                this@CompleteAccountActivity,
//                                error,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                        Toast.makeText(
//                            this@CompleteAccountActivity,
//                            t.message,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageButton0.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        val currentTimeMillis = System.currentTimeMillis()
        val imageFileName = "image_$currentTimeMillis.jpg"

        val imageRef = storageReference.child("images/$imageFileName")

        imageButton0.isDrawingCacheEnabled = true
        imageButton0.buildDrawingCache()
        val bitmap = imageButton0.drawingCache

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

                    AppReference.setImageUrl(this, imageUrl)

                    val username = intent.getStringExtra("username").toString().trim()
                    val email = intent.getStringExtra("email").toString().trim()
                    val password = intent.getStringExtra("password").toString().trim()

                    val phone = edt_phone.text.toString().trim()

                    val firstSkill = edt_first_skill.text.toString().trim()
                    val secondSkill = edt_second_skill.text.toString().trim()
                    val thirdSkill = edt_third_skill.text.toString().trim()
                    val fourthSkill = edt_fourth_skill.text.toString().trim()
                    val fifthSkill = edt_fifth_skill.text.toString().trim()

                    val skills = listOf(firstSkill, secondSkill, thirdSkill, fourthSkill, fifthSkill)

                    val resumeUrl = null

                    val image= AppReference.getImageUrl(this)


                    val data = SignUpRequest(username, email, password, phone, skills, resumeUrl.toString(), image)
                    RetrofitClient.instance.register(data)
                        .enqueue(object : Callback<RegisterResponse> {
                            override fun onResponse(
                                call: Call<RegisterResponse>,
                                response: Response<RegisterResponse>
                            ) {
                                if (response.isSuccessful){
                                    val defaultResponse = response.body()
                                    if (defaultResponse != null){
                                        val message = defaultResponse.message
                                        Toast.makeText(
                                            baseContext,
                                            message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(Intent(this@CompleteAccountActivity, LoginActivity::class.java))
                                        finish()
                                        Log.e("Data = " , "$username $email $password $phone $resumeUrl $imageUrl")
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Invalid response from server",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    val error = response.errorBody()?.string()
                                    Toast.makeText(
                                        this@CompleteAccountActivity,
                                        error,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@CompleteAccountActivity,
                                    t.message,
                                    Toast.LENGTH_LONG
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
}