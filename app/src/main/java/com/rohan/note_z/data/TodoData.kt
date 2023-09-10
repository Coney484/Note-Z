package com.rohan.note_z.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String,
    var priority: Priority,
    var description: String
) : Parcelable
