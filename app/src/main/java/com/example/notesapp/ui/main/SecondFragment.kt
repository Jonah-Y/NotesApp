package com.example.notesapp

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notesapp.databinding.FragmentSecondBinding
import com.example.notesapp.ui.main.NoteViewModel
import com.example.notesapp.data.Note


/**
 * This fragment displays the full editable contents of a note.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModel.Factory
    }
    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.setText(arguments?.getString("title") ?: "New Note")
        binding.note.setText(arguments?.getString("content") ?: "Add Text")

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            saveNote()
            isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun saveNote() {
        val id = arguments?.getInt("id") ?: 0   //if 0 a new note will be created
        val title = binding.title.text.toString()
        val content = binding.note.text.toString()
        val timestamp = System.currentTimeMillis()

        note = Note(id, title, content, timestamp)
        viewModel.saveNote(note)
    }
}