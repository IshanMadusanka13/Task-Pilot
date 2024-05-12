package com.example.taskpilot.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskpilot.database.entities.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks():List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM tasks WHERE name like :taskName")
    suspend fun getTaskByName(taskName: String):List<Task>

    @Query("SELECT * FROM tasks WHERE deadline = :todayDate")
    suspend fun getTodayTasks(todayDate: String): List<Task>

    @Query("SELECT * FROM tasks WHERE strftime('%Y-%m-%d', deadline) < date('now')")
    suspend fun getPastTasks(): List<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}