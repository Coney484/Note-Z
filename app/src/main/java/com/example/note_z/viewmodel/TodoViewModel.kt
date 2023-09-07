package com.example.note_z.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_z.data.TodoData
import com.example.note_z.data.TodoDatabase
import com.example.note_z.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    private val repo: TodoRepository

    val getAllData: LiveData<List<TodoData>>

    init {
        repo = TodoRepository(todoDao)
        getAllData = repo.getAllData
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

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAllData()
        }
    }
}