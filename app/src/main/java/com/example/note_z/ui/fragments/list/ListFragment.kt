package com.example.note_z.ui.fragments.list

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_z.R
import com.example.note_z.adapters.NotesAdapter
import com.example.note_z.databinding.FragmentListBinding
import com.example.note_z.ui.fragments.SharedViewModel
import com.example.note_z.viewmodel.TodoViewModel

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val mViewModel: TodoViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: NotesAdapter by lazy {
        NotesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        setupAdapter()
        setupObserver()
        setHasOptionsMenu(true)
    }

    private fun setupObserver() {
        mViewModel.getAllData.observe(viewLifecycleOwner) { noteData ->
            mSharedViewModel.checkIfNotesExistOrNot(noteData)
            adapter.setData(noteData)

        }

        mSharedViewModel.emptyNotesLiveData.observe(viewLifecycleOwner) {
            showNoNotesToShow(it)
        }
    }

    private fun showNoNotesToShow(allNotesEmpty: Boolean) {
        if (allNotesEmpty) {
            binding.apply {
                noDataIv.visibility = View.VISIBLE
                noDataTv.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                noDataIv.visibility = View.GONE
                noDataTv.visibility = View.GONE
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAllNotes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes") { _, _ ->
            mViewModel.deleteAllData()
            Toast.makeText(
                requireContext(),
                "All Notes Deleted Successfully",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton("No") { _, _ ->
        }

        builder.setTitle("Delete Everything ?")
        builder.setMessage("Do you want to Delete All Notes ?")
        builder.create().show()
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    private fun listeners() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }
    }
}