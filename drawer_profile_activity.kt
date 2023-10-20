package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class drawer_profile_activity : AppCompatActivity() {

    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_profile)


        back = findViewById(R.id.back_button)

        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }
    }
}