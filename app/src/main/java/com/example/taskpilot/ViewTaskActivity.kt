package com.example.taskpilot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.entities.Task
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewTaskActivity : AppCompatActivity() {

    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val repository = TaskRepository(TaskDatabase.getDatabase(this))
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val taskId = intent.getIntExtra("taskId", -1)

        val taskName = findViewById<TextView>(R.id.txtTaskName)
        val deadline = findViewById<TextView>(R.id.txtDeadline)
        val priority = findViewById<TextView>(R.id.txtPriority)
        val description = findViewById<TextView>(R.id.txtDescription)

        CoroutineScope(Dispatchers.IO).launch {
            task = repository.getTaskById(taskId)!!

            withContext(Dispatchers.Main) {
                if (task != null) {
                    taskName.text = task.name
                    deadline.text = task.deadline
                    priority.text = task.priority
                    description.text = task.description
                }
            }
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val intent = Intent(this@ViewTaskActivity, UpdateTaskActivity::class.java)
            intent.putExtra("taskId", taskId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                repository.delete(task)
                val data = repository.getAllTasks()
                withContext(Dispatchers.Main) {
                    viewModel.setData(data)
                }
            }
            startActivity(Intent(this@ViewTaskActivity, MainActivity::class.java))
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this@ViewTaskActivity, MainActivity::class.java))
        }
    }
}