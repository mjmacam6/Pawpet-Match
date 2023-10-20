package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myproject.databinding.ActivityCreateNotesBinding
import com.example.myproject.databinding.ActivityNotesUpdateBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesUpdateBinding
    private lateinit var db: NotesDataBaseHelper
    private var noteId: Int = -1 //represent that the value is empty


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDataBaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()

            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updateNote = Note(noteId, newTitle, newContent)

            Handler(Looper.getMainLooper()).postDelayed({
                db.updateNote(updateNote)
                dialog.dismiss()
                finish()
                Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
            }, 2000)

        }
    }
}