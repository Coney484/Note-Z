package com.example.note_z.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

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
}