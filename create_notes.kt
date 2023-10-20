package com.example.myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.databinding.ActivityCreateNotesBinding

class create_notes : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNotesBinding
    private lateinit var db: NotesDataBaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var back: ImageView
    private lateinit var orderBy : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sortBy = "ASC"
        orderBy = findViewById(R.id.order)
        db = NotesDataBaseHelper(this)

        back = findViewById(R.id.back_button)
        notesAdapter = NotesAdapter(db.getAllNotes(sortBy), this)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        back.setOnClickListener {

            back.setOnClickListener {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }

        binding.order.setOnClickListener {
            if (sortBy == "ASC") {
                sortBy = "DESC"
                binding.order.setImageResource(R.drawable.dr_desc)
                Toast.makeText(this, "Switched to Descending Order", Toast.LENGTH_SHORT).show()
            } else {
                sortBy = "ASC"
                binding.order.setImageResource(R.drawable.dr_asc)
                Toast.makeText(this, "Switched to Ascending Order", Toast.LENGTH_SHORT).show()
            }
            onResume(sortBy)
        }

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)
        }
    }
    private fun onResume(x : String){
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes(x))
    }
}