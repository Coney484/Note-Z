package com.example.note_z.ui.fragments.update

import android.app.AlertDialog
import android.os.Bundle
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
import androidx.navigation.fragment.navArgs
import com.example.note_z.R
import com.example.note_z.data.TodoData
import com.example.note_z.databinding.FragmentUpdateBinding
import com.example.note_z.viewmodel.SharedViewModel
import com.example.note_z.viewmodel.TodoViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var binding: FragmentUpdateBinding
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mViewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> updateItem()
                    R.id.menu_delete -> deleteItem()
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupArguments()
    }


    private fun setupArguments() {
        binding.apply {
            etUpdateTitle.setText(args.currentItem.title)
            etUpdateDescription.setText(args.currentItem.description)
            updateSpinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
            updateSpinner.onItemSelectedListener = mSharedViewModel.listener

        }
    }

    private fun updateItem() {
        val title = binding.etUpdateTitle.text.toString()
        val description = binding.etUpdateDescription.text.toString()
        val getPriority = binding.updateSpinner.selectedItem.toString()

        val isValid = mSharedViewModel.validateData(title, description)

        if (isValid) {
            val updatedNote =
                TodoData(
                    args.currentItem.id,
                    title,
                    mSharedViewModel.parsePriority(getPriority),
                    description
                )

            mViewModel.updateData(updatedNote)
            Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "All Fields Required !", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes") { _, _ ->
            mViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Note ${args.currentItem.title} Deleted Successfully",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        builder.setNegativeButton("No") { _, _ ->
        }

        builder.setTitle("Delete ${args.currentItem.title}")
        builder.setMessage("Do you want to Delete ${args.currentItem.title} Note ?")
        builder.create().show()
    }
}