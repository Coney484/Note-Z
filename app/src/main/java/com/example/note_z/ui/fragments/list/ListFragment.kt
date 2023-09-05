package com.example.note_z.ui.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_z.R
import com.example.note_z.adapters.NotesAdapter
import com.example.note_z.databinding.FragmentListBinding
import com.example.note_z.viewmodel.TodoViewModel

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val mViewModel: TodoViewModel by viewModels()

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
            adapter.setData(noteData)

        }
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