package com.example.taskpilot.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskpilot.database.entities.Task
import com.example.taskpilot.database.repositories.TaskRepository

class TaskViewModel : ViewModel() {

    private val _data = MutableLiveData<List<Task>>()
    val data:LiveData<List<Task>> = _data
    fun setData(data:List<Task>){
        _data.value = data
    }

}