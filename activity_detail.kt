package com.example.myproject

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class activity_detail : AppCompatActivity() {

    private lateinit var back: ImageView
    private lateinit var detailLocation: TextView
    private lateinit var detailDesc : TextView
    private lateinit var detailTitle : TextView
    private lateinit var detailImage: ImageView
    private lateinit var detailPrice : TextView
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var editButton: FloatingActionButton
    private lateinit var uploader: TextView
    //step 5 delete button
    private var key = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        back = findViewById(R.id.back_button)
        detailLocation = findViewById(R.id.detailLocation)
        detailPrice = findViewById(R.id.detailPrice);
        detailDesc = findViewById(R.id.detailDesc);
        detailTitle = findViewById(R.id.detailTitle);
        detailImage = findViewById(R.id.detailImage);
        deleteButton = findViewById(R.id.deleteButton)
        editButton = findViewById(R.id.editButton)
        uploader = findViewById(R.id.detailUploader)


        val bundle = intent.extras
        if (bundle != null) {
            val description = bundle.getString("Description")
            val title = bundle.getString("Title")
            val price = bundle.getString("Price")
            val location = bundle.getString("Location")

            detailDesc.text = removePrefix("Description: ", description)
            detailTitle.text = removePrefix("Title: ", title)
            detailPrice.text = removePrefix("Price: ", price)
            detailLocation.text = removePrefix("Location: ", location)
            uploader.text = bundle.getString("Uploader")
            //step 6 delete button
            key = bundle.getString("Key") ?: ""
            imageUrl = bundle.getString("Image") ?: ""
            Glide.with(this).load(bundle.getString("Image")).into(detailImage)
        }

        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }


        //step 7 delete button
        deleteButton.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Uploads")
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().addOnSuccessListener {

                reference.child(key).removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


        editButton.setOnClickListener{
            val intent = Intent(this, UpdateActivity::class.java)
                .putExtra("Title", detailTitle.text.toString())
                .putExtra("Price", detailPrice.text.toString())
                .putExtra("Description", detailDesc.text.toString())
                .putExtra("Location", detailLocation.text.toString())
                .putExtra("Key", key)
                .putExtra("Image", imageUrl)
            startActivity(intent)
            finish();
        }


    }

    fun removePrefix(prefix: String, text: String?): String {
        return text?.removePrefix(prefix) ?: ""
    }
}
