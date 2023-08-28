package com.example.note_z.ui.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.note_z.R
import com.example.note_z.data.Priority
import com.example.note_z.data.TodoData
import com.example.note_z.databinding.FragmentAddBinding
import com.example.note_z.viewmodel.TodoViewModel


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private val mViewModel: TodoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        var noteTitle = binding.etTitle.text.toString()
        var notePriority = binding.spinner.selectedItem.toString()
        var noteDescription = binding.etMultiLine.text.toString()

        val isValid = validateData(noteTitle, noteDescription)
        if (isValid) {
            val newData = TodoData(0, noteTitle, parsePriority(notePriority), noteDescription)
            mViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Data Added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "All Fields Required !", Toast.LENGTH_SHORT).show()

        }
    }

    private fun validateData(noteTitle: String, noteDescription: String): Boolean {
        return if (TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
            false
        } else !(noteTitle.isEmpty() || noteDescription.isEmpty())
    }

    private fun parsePriority(notePriority: String): Priority {
        return when (notePriority) {
            "High" -> Priority.HIGH
            "Medium" -> Priority.MEDIUM
            "Low" -> Priority.LOW
            else -> Priority.HIGH
        }
    }
}