package com.example.notesapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.R


class RecyclerAdapter(notes: List<Note>, onItemClick: (Note) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var notes: List<Note>
    private val onItemClick: (Note) -> Unit
    init {
        this.notes = notes
        this.onItemClick = onItemClick
    }

    fun updateData(newNotes: List<Note>) {
        this.notes = newNotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_preview, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.preview.text = notes[position].content

        holder.itemView.setOnClickListener {
            onItemClick(notes[position])
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView.findViewById(R.id.title)
        var preview : TextView = itemView.findViewById(R.id.preview)
    }

}