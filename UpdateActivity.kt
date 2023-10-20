package com.example.myproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class   UpdateActivity : AppCompatActivity() {

    private lateinit var uri : Uri
    private lateinit var back: ImageView
    private lateinit var updateImage: ImageView
    private lateinit var updateButton : Button
    private lateinit var updateTitle : EditText
    private lateinit var updateDesc : EditText
    private lateinit var updatePrice : EditText
    private lateinit var updateLocation : EditText
    private lateinit var imageUrl: String
    private lateinit var oldImageUrl: String
    private lateinit var key: String
    private lateinit var mDatabaseRef: DatabaseReference
    private var imageSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        uri = Uri.parse("")
        updateButton = findViewById(R.id.updateButton)
        updateDesc = findViewById(R.id.updateDesc)
        updateImage = findViewById(R.id.updateImage)
        updatePrice = findViewById(R.id.updatePrice)
        updateTitle = findViewById(R.id.updateTitle)
        updateLocation = findViewById(R.id.updateLocation)
        back = findViewById(R.id.back_button)


        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data!!
                updateImage.setImageURI(uri)
                imageSelected = true
            } else {
                Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show();
            }
        };

        val bundle = intent.extras
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("Image")).into(updateImage)
            updateDesc.text = Editable.Factory.getInstance().newEditable(bundle.getString("Description"))
            updateTitle.text = Editable.Factory.getInstance().newEditable(bundle.getString("Title"))
            updatePrice.text = Editable.Factory.getInstance().newEditable(bundle.getString("Price"))
            updateLocation.text = Editable.Factory.getInstance().newEditable(bundle.getString("Location"))
            key = bundle.getString("Key") ?: ""
            oldImageUrl = bundle.getString("Image") ?: ""
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads").child(key);

        updateImage.setOnClickListener { view: View? ->
            val photoPicker = Intent()
            photoPicker.action = Intent.ACTION_GET_CONTENT
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        updateButton.setOnClickListener {

            if (validateInputs()) {
                saveData()
            } else {
                // Show a warning to the user
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun validateInputs(): Boolean {
        val titleText = updateTitle.text.toString()
        val descriptionText = updateDesc.text.toString()
        val priceText = updatePrice.text.toString()
        val locationText = updateLocation.text.toString()

        return titleText.isNotEmpty() && descriptionText.isNotEmpty() && priceText.isNotEmpty() && locationText.isNotEmpty()
    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().getReference().child("uploadImages")
            .child(uri.lastPathSegment.toString());

        val builder = AlertDialog.Builder(this@UpdateActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                uriTask.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        val urlImage = downloadTask.result.toString()
                        imageUrl = urlImage.toString();
                        dialog.dismiss()
                        updateData()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the case when getting the download URL fails
                        dialog.dismiss()

                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the case when uploading the image fails
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.update_activity, Home_fragment())
                transaction.commit()
                finish()
                dialog.dismiss()
            }
    }

    private fun updateData() {
        val title = updateTitle.text.toString().trim()
        val priceStr = updatePrice.text.toString().trim()
        val desc = updateDesc.text.toString().trim()
        val loc = updateLocation.text.toString().trim()
        priceStr.toIntOrNull() ?: 0


        val dataUpload = Data_Upload(title, priceStr.toLong(), desc, imageUrl, loc,)

        mDatabaseRef.setValue(dataUpload)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                    reference.delete()
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

}