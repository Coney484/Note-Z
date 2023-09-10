package com.rohan.note_z.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rohan.note_z.data.TodoData
import com.rohan.note_z.data.TodoDatabase
import com.rohan.note_z.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    private val repo: TodoRepository

    val sortByHighPriority: LiveData<List<TodoData>>
    val sortByLowPriority: LiveData<List<TodoData>>
    val sortByMediumPriority: LiveData<List<TodoData>>
    val getAllData: LiveData<List<TodoData>>

    init {
        repo = TodoRepository(todoDao)
        getAllData = repo.getAllData
        sortByHighPriority = repo.sortByHighPriority
        sortByLowPriority = repo.sortByLowPriority
        sortByMediumPriority = repo.sortByMediumPriority
    }

    fun insertData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData(todoData)
        }
    }

    fun updateData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateData(todoData)
        }
    }


    fun deleteData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteData(todoData)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllData()
        }
    }


    fun searchNotes(searchQuery: String): LiveData<List<TodoData>> {
        return repo.searchNotes(searchQuery)
    }
}