package com.rohan.note_z.ui.fragments.add

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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.rohan.note_z.R
import com.rohan.note_z.data.Priority
import com.rohan.note_z.data.TodoData
import com.rohan.note_z.databinding.FragmentAddBinding
import com.rohan.note_z.viewmodel.TodoViewModel


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
        setupOptionsMenu()
    }

    private fun setupOptionsMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_add) {
                    insertDataToDb()
                } else if(menuItem.itemId == android.R.id.home){
                    requireActivity().onBackPressed()

                }

                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun insertDataToDb() {
        val noteTitle = binding.etTitle.text.toString()
        val notePriority = binding.spinner.selectedItem.toString()
        val noteDescription = binding.etMultiLine.text.toString()

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