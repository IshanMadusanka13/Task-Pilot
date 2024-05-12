package com.example.taskpilot.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskpilot.database.entities.Task
import com.example.taskpilot.database.repositories.TaskRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewModel : ViewModel() {

    private val _data = MutableLiveData<List<Task>>()
    val data:LiveData<List<Task>> = _data
    fun setData(data:List<Task>){
        _data.value = data
    }

    fun sortTasksByDeadline() {
        _data.value = _data.value?.sortedBy { parseDate(it.deadline) }
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    fun sortTasksByPriority() {
        _data.value = _data.value?.sortedWith(compareByDescending<Task> {
            when (it.priority) {
                "High" -> 3
                "Medium" -> 2
                "Low" -> 1
                else -> 0
            }
        })
    }



}