package com.example.vamsisnotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.vamsisnotes.Models.Note
import com.example.vamsisnotes.databinding.ActivityNewNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NewNote : AppCompatActivity() {

    private lateinit var note: Note
    private lateinit var oldNote: Note
    var isUpdate = false

    private lateinit var binding: ActivityNewNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.titleNote.setText(oldNote.title)
            binding.note.setText(oldNote.description)
            isUpdate = true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        binding.check.setOnClickListener {
            val title = binding.titleNote.text.toString()
            val note_desc = binding.note.text.toString()

            if (title.isNotEmpty() || note_desc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:MM a")
                if (isUpdate) {
                    note = Note(oldNote.id, title, note_desc, formatter.format(Date()))
                } else {
                    note = Note(null, title, note_desc, formatter.format(Date()))

                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Text can't be blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
        binding.back.setOnClickListener{
            onBackPressed()
        }
    }
}