package com.example.myproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var maUth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maUth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.btConfirm.setOnClickListener {
            val loginUserEmail = binding.etEmail.text.toString()
            val loginUserPassword = binding.etPassword.text.toString()

            if (loginUserEmail.isNotEmpty() && loginUserPassword.isNotEmpty()) {
                if (loginUserEmail.endsWith("@gmail.com")) {
                    logIn(loginUserEmail, loginUserPassword)
                } else {
                    Toast.makeText(this@MainActivity, "Invalid email address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createAcc.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
            finish()
        }
    }

    /*private fun loginUser(UserEmail: String, UserPassword: String) {
        if (!isValidEmail(UserEmail)) {
            Toast.makeText(this@MainActivity, "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.orderByChild("username").equalTo(UserEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            if (userData != null && userData.password == UserPassword) {
                                Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                                finish()
                                return
                            }
                        }
                    }
                    Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }*/

    private fun logIn(email: String, password: String){
        maUth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                    finish()
                }

                else{
                    Toast.makeText(this@MainActivity, "Log In Failed", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@gmail.com".toRegex()
        return emailPattern.matches(email)
    }
}