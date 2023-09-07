package com.example.note_z.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<TodoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(todoData: TodoData)

    @Update
    suspend fun updateData(todoData: TodoData)

    @Delete
    suspend fun deleteData(todoData: TodoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllData()


}