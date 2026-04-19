package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.notesapp.databinding.FragmentSecondBinding
import com.example.notesapp.ui.main.NoteViewModel
import com.example.notesapp.data.Note
import com.google.android.material.snackbar.Snackbar

const val DEFAULT_TITLE = "New Note"
const val DEFAULT_NOTE = "Add Text"

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
    private lateinit var startingTitle: String
    private lateinit var startingContent: String
    private var deletionInProgress: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startingTitle = arguments?.getString("title") ?: DEFAULT_TITLE
        startingContent = arguments?.getString("content") ?: DEFAULT_NOTE
        binding.title.setText(startingTitle)
        binding.note.setText(startingContent)

        // set the click handler for the overflow menu (deletion)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_second, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete -> {
                        deleteNote(requireView())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // this handles back and exit inputs by saving the note
    override fun onPause() {
        super.onPause()
        if (!deletionInProgress)
            saveNote()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveNote() {
        val title = binding.title.text.toString()
        val content = binding.note.text.toString()

        //do not save an empty new note or a note that was not modified
        if ((title == startingTitle && content == startingContent)
            || arguments?.getInt("id") == 0
            && (title == DEFAULT_TITLE || title.isEmpty())
            && (content == DEFAULT_NOTE || content.isEmpty())) {
            return
        }

        val id = arguments?.getInt("id") ?: 0   //if 0 a new note will be created
        val timestamp = System.currentTimeMillis()

        note = Note(id, title, content, timestamp)
        viewModel.saveNote(note)
    }

    private fun deleteNote(view: View) {
        deletionInProgress = true
        val title = binding.title.text.toString()

        // DELETE FROM notes WHERE id = noteId
        viewModel.deleteNote(Note(arguments?.getInt("id") ?: 0, "", ""))

        Snackbar.make(view, "$title Deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show()

        findNavController().popBackStack() // return to Notes List Fragment
    }
}