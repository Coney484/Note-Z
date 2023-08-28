package com.example.note_z.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String,
    var priority: Priority,
    var description: String
)
