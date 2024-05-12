package com.example.taskpilot.database.repositories

import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.daos.TaskDao
import com.example.taskpilot.database.entities.Task

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
}