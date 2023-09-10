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
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note_z.R
import com.example.note_z.adapters.NotesAdapter
import com.example.note_z.databinding.FragmentListBinding
import com.example.note_z.observeOnce
import com.example.note_z.viewmodel.SharedViewModel
import com.example.note_z.viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding

    private val mViewModel: TodoViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter: NotesAdapter by lazy {
        NotesAdapter()
    }

    private var toggleMenuItem: MenuItem? = null


    private var isStaggeredLayoutManager: Boolean = true


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
        setupMenuOptions()
    }

    private fun setupMenuOptions() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)

                toggleMenuItem = menu.findItem(R.id.menu_toggle)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> deleteAllNotes()
                    R.id.menu_priority_high -> mViewModel.sortByHighPriority.observe(
                        viewLifecycleOwner
                    ) {
                        adapter.setData(it)
                    }

                    R.id.menu_priority_low -> mViewModel.sortByLowPriority.observe(
                        viewLifecycleOwner
                    ) {
                        adapter.setData(it)
                    }

                    R.id.menu_priority_medium -> mViewModel.sortByMediumPriority.observe(
                        viewLifecycleOwner
                    ) {
                        adapter.setData(it)
                    }

                    R.id.menu_toggle -> {
                        toggleLayoutManager()
                    }

                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun toggleLayoutManager() {
        isStaggeredLayoutManager = !isStaggeredLayoutManager
        val layoutManager = if (isStaggeredLayoutManager) {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.recyclerView.layoutManager = layoutManager

        // Toggle the icon of the toggle button based on the current layout manager
        if (toggleMenuItem != null) {
            toggleMenuItem!!.icon = if (isStaggeredLayoutManager) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_view_icon)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_linear_view_icon)
            }
        }

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
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerView.itemAnimator = LandingAnimator().apply {
            addDuration = 350
        }
        swipeToDelete(binding.recyclerView)
    }


    private fun listeners() {
    }

    override fun onQueryTextSubmit(searchQuery: String?): Boolean {
        if (!searchQuery.isNullOrEmpty()) {
            searchNotes(searchQuery)
        } else {
            mViewModel.getAllData.value?.let { adapter.setData(it) }
        }
        return true
    }


    override fun onQueryTextChange(searchQuery: String?): Boolean {
        if (!searchQuery.isNullOrEmpty()) {
            searchNotes(searchQuery)
        }

        return true
    }

    private fun searchNotes(query: String) {
        var searchQuery: String = query
        searchQuery = "%${searchQuery}%"
        mViewModel.searchNotes(searchQuery).observeOnce(viewLifecycleOwner) {
            it?.let {
                adapter.setData(it)
            }
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.asyncListDiffer.currentList[viewHolder.adapterPosition]
                mViewModel.deleteData(itemToDelete)
                recyclerView.itemAnimator = SlideInUpAnimator().apply {
                    addDuration = 300
                }
                view?.let {
                    Snackbar.make(it, "Note Deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            mViewModel.insertData(itemToDelete)
                        }
                        show()
                    }
                }

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


}