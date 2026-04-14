package com.example.notesapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notesapp.data.Note
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentListOfNotesListBinding
import kotlin.getValue

/**
 * A fragment representing a list of note items.
 */
class ListOfNotesFragment : Fragment() {

    private var columnCount = 1

    private var _binding: FragmentListOfNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOfNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteAdapter = RecyclerAdapter(viewModel.notes.value ?: emptyList(), ::onNoteClick)
        val recyclerView = binding.listOfNotes

        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = noteAdapter
            }
        }

        viewModel.notes.observe(viewLifecycleOwner) { updatedNotes ->
            noteAdapter.updateData(updatedNotes)
        }

    }

    fun onNoteClick(selectedNote: Note): Unit{
        val bundle = Bundle()
        bundle.putInt("id", selectedNote.id)
        bundle.putString("title", selectedNote.title)
        bundle.putString("content", selectedNote.content)
        bundle.putLong("timestamp", selectedNote.timestamp)

        val controller: NavController = NavHostFragment.findNavController(this)
        controller.navigate(R.id.action_ListOfNotesFragment_to_SecondFragment, bundle)
    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListOfNotesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}