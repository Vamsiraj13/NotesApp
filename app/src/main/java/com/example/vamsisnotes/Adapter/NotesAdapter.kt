package com.example.vamsisnotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.vamsisnotes.Models.Note
import com.example.vamsisnotes.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, private val listener: NotesItemClickedListener): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val notesLayout: CardView = itemView.findViewById(R.id.cardView)
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.desc)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.description.text = currentNote.description
        holder.date.text = currentNote.date
        holder.date.isSelected = true
        holder.notesLayout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor()))

        holder.notesLayout.setOnClickListener{
            listener.onItemClicked(notesList[holder.adapterPosition])
        }
        holder.notesLayout.setOnLongClickListener{
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.notesLayout)
            true
        }
    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String){
        notesList.clear()
        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true || item.description?.lowercase()?.contains(search.lowercase()) == true){
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor(): Int {
        val colorList = ArrayList<Int>()
        colorList.add(R.color.color1)
        colorList.add(R.color.color2)
        colorList.add(R.color.color3)
        colorList.add(R.color.color4)
        colorList.add(R.color.color5)
        colorList.add(R.color.color6)
        colorList.add(R.color.color7)

        val randomIndex = Random.nextInt(0,colorList.size)
        return colorList[randomIndex]

    }

    interface NotesItemClickedListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}