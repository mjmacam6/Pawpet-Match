package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout

class drawer_settings : AppCompatActivity() {

    private lateinit var back: ImageView
    private lateinit var acc : RelativeLayout
    private lateinit var privacy : RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_settings)

        back = findViewById(R.id.back_button)
        acc = findViewById(R.id.accounts)
        privacy = findViewById(R.id.privacy)


        privacy.setOnClickListener {
            val intent = Intent(this, settings_privacy::class.java)
            startActivity(intent)
        }
        acc.setOnClickListener {
            val intent = Intent(this, settings_account::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }

    }
}