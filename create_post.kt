package com.example.myproject

import android.content.Intent
import android.icu.text.DateFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date


class create_post : AppCompatActivity() {

    private lateinit var imageUri : Uri
    private lateinit var back: ImageView
    private lateinit var upload_post: ImageView
    private lateinit var create : Button
    private lateinit var title : EditText
    private lateinit var description : EditText
    private lateinit var location : EditText
    private lateinit var price : EditText
    private lateinit var imageUrl: String

    //ITO UNG SA IMAGE
    private var imageSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        title = findViewById(R.id.cp_title)
        description = findViewById(R.id.cp_description)
        price = findViewById(R.id.cp_price)
        location = findViewById(R.id.cp_location)
        create = findViewById(R.id.create_post_button)
        upload_post = findViewById(R.id.upload_photo)
        back = findViewById(R.id.back_button)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data!!.data!!
                upload_post.setImageURI(imageUri)
                imageSelected = true
            } else {
                Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show()
            }
        }

        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }

        upload_post.setOnClickListener { view: View? ->
            val photoPicker = Intent()
            photoPicker.action = Intent.ACTION_GET_CONTENT
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        create.setOnClickListener {
            if (validateInputs() && imageSelected) {
                saveData()
            } else {
                // Show a warning to the user
                Toast.makeText(this, if (!imageSelected) "Please select an image" else "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val titleText = title.text.toString()
        val descriptionText = description.text.toString()
        val priceText = price.text.toString()
        val locationText = location.text.toString()

        return titleText.isNotEmpty() && descriptionText.isNotEmpty() && priceText.isNotEmpty() && locationText.isNotEmpty()
    }


    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().getReference("uploadImages")
            .child(imageUri.lastPathSegment.toString());
        val builder = AlertDialog.Builder(this@create_post)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                uriTask.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        val urlImage = downloadTask.result.toString()
                        imageUrl = urlImage
                        dialog.dismiss()
                        uploadData()
                    } else {
                        // Handle the case when getting the download URL fails
                        dialog.dismiss()
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the case when uploading the image fails
                dialog.dismiss()
            }
    }
    private fun uploadData() {
        val title = title.text.toString()
        val priceStr = price.text.toString()
        val desc = description.text.toString()
        val loc = location.text.toString()
        priceStr.toIntOrNull() ?: 0

        val dataUpload = Data_Upload(title, priceStr.toLong(), desc, imageUrl, loc)
        val currentDate: String = DateFormat.getDateTimeInstance().format(Date())
        val databaseReference = FirebaseDatabase.getInstance().getReference("Uploads").child(currentDate)


        databaseReference.setValue(dataUpload)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                    finish()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.create_post_activity, Home_fragment())
                    transaction.commit()
                    finish();
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}
