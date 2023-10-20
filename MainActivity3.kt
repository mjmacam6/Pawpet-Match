package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myproject.databinding.ActivityMain3Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.btConfirm2.setOnClickListener {
            val signupEmail = binding.etCreateUser.text.toString()
            val signupPassword = binding.etCreatePw01.text.toString()
            val signupConfirmPassword = binding.etCreatePw02.text.toString()

            if (validateInput(signupEmail, signupPassword, signupConfirmPassword)) {
                signupUser(signupEmail, signupPassword)
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@MainActivity3, MainActivity::class.java))
            finish()
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || !email.contains("@gmail.com")) {
            showToast("Invalid email format")
            return false
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Password fields are mandatory")
            return false
        }

        if (password != confirmPassword) {
            showToast("Passwords don't match")
            return false
        }

        return true
    }

    private fun signupUser(signupUserEmail: String, signupUserPassword: String) {
        databaseReference.orderByChild("username").equalTo(signupUserEmail).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData = UserData(id, signupUserEmail, signupUserPassword)
                    databaseReference.child(id!!).setValue(userData)
                    showToast("Registration successful")
                    startActivity(Intent(this@MainActivity3, MainActivity::class.java))
                    finish()
                } else {
                    showToast("User already exists")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Database Error: ${databaseError.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity3, message, Toast.LENGTH_SHORT).show()
    }
}