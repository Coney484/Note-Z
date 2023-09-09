package com.example.note_z.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.note_z.R
import com.example.note_z.data.Priority
import com.example.note_z.data.TodoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyNotesLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun checkIfNotesExistOrNot(todoData: List<TodoData>) {
        emptyNotesLiveData.postValue(todoData.isEmpty())
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }

                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.yellow
                        )
                    )
                }

                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.blue
                        )
                    )
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}

    }


    fun validateData(noteTitle: String, noteDescription: String): Boolean {
        return if (TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteDescription)) {
            false
        } else !(noteTitle.isEmpty() || noteDescription.isEmpty())
    }

    fun parsePriority(notePriority: String): Priority {
        return when (notePriority) {
            "High" -> Priority.HIGH
            "Medium" -> Priority.MEDIUM
            "Low" -> Priority.LOW
            else -> Priority.HIGH
        }
    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }


}