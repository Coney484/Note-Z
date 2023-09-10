package com.rohan.note_z.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    val sortByHighPriority: LiveData<List<TodoData>> = todoDao.sortByHigherPriority()
    val sortByLowPriority: LiveData<List<TodoData>> = todoDao.sortByLowPriority()
    val sortByMediumPriority: LiveData<List<TodoData>> = todoDao.sortByMediumPriority()

    val getAllData: LiveData<List<TodoData>> = todoDao.getAllData()

    suspend fun insertData(todoData: TodoData) {
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData) {
        todoDao.updateData(todoData)
    }

    suspend fun deleteData(todoData: TodoData) {
        todoDao.deleteData(todoData)
    }

    suspend fun deleteAllData() {
        todoDao.deleteAllData()
    }

    fun searchNotes(searchQuery: String): LiveData<List<TodoData>> {
        return todoDao.searchNote(searchQuery)
    }
}