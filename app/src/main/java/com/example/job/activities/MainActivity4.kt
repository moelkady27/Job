package com.example.job.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.job.AppReference
import com.example.job.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main4.*
import java.io.ByteArrayOutputStream


class MainActivity4 : AppCompatActivity() {

    private val PICK_IMAGE = 1



    private lateinit var storageReference: StorageReference




    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) {
            super.onBackPressed()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)


        storageReference = FirebaseStorage.getInstance().reference




        imageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        button2.setOnClickListener {
            uploadImageToFirebaseStorage()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)
        }
    }



    private fun uploadImageToFirebaseStorage() {
        val currentTimeMillis = System.currentTimeMillis()
        val imageFileName = "image_$currentTimeMillis.jpg"

        val imageRef = storageReference.child("images/$imageFileName")

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = imageView.drawingCache

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
                }
            }
        }.addOnFailureListener { e ->
            // Handle any errors that may occur.
            Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}