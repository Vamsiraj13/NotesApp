package com.example.vamsisnotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vamsisnotes.Adapter.NotesAdapter
import com.example.vamsisnotes.Database.NoteDatabase
import com.example.vamsisnotes.Database.NoteDatabase.Companion.getDatabase
import com.example.vamsisnotes.Models.Note
import com.example.vamsisnotes.Models.NotesVIewModel
import com.example.vamsisnotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NotesAdapter.NotesItemClickedListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NotesVIewModel
    lateinit var madapter: NotesAdapter
    lateinit var selectedNote: Note
    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val note = it.data?.getSerializableExtra("note") as Note
            if(note!=null){
                viewModel.update(note)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLayout()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NotesVIewModel::class.java]
        
        viewModel.allnotes.observe(this) { list ->
            list?.let {
                madapter.updateList(list)
            }
        }
        database =  getDatabase(this)

    }

    private fun initializeLayout() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        madapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = madapter
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val note = it.data?.getSerializableExtra("note") as Note
                if(note!=null){
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fab.setOnClickListener{
            val intent = Intent(this, NewNote::class.java)
            getContent.launch(intent)
        }

        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null){
                    madapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this, NewNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch((intent))
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }
    private fun popUpDisplay(cardView: CardView){
        val popUp = PopupMenu(this, cardView)
        popUp.setOnMenuItemClickListener(this)
        popUp.inflate(R.menu.popup_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}


