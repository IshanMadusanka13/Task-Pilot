package com.example.taskpilot.database.repositories

import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.daos.TaskDao
import com.example.taskpilot.database.entities.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskRepository(private val taskDatabase: TaskDatabase) {

    fun getAllTasks():List<Task> = taskDatabase.taskDao().getAllTasks()

    suspend fun insert(task: Task) {
        taskDatabase.taskDao().insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDatabase.taskDao().updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDatabase.taskDao().deleteTask(task)
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return taskDatabase.taskDao().getTaskById(taskId)
    }

    suspend fun getTaskByName(taskName: String): List<Task> {
        return taskDatabase.taskDao().getTaskByName("%$taskName%")
    }

    suspend fun getTodayTasks(): List<Task> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val todayDate = dateFormat.format(Date())
        return taskDatabase.taskDao().getTodayTasks(todayDate)
    }

    suspend fun getPastTasks(): List<Task> {
        return taskDatabase.taskDao().getPastTasks()
    }
}
