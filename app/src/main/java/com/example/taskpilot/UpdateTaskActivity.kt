package com.example.taskpilot

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        val repository = TaskRepository(TaskDatabase.getDatabase(this))
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        val taskId = intent.getIntExtra("taskId", -1)

        val nameEditText = findViewById<EditText>(R.id.nameEdit)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEdit)
        val deadlineEditText = findViewById<EditText>(R.id.deadlineEdit)
        val prioritySpinner = findViewById<Spinner>(R.id.priorityEdit)
        val editTaskButton = findViewById<Button>(R.id.btnEdit)

        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                deadlineEditText.setText(dateFormat.format(calendar.time))
            }

        deadlineEditText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        val priorities = arrayOf("Low", "Medium", "High")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            task = repository.getTaskById(taskId)!!

            withContext(Dispatchers.Main) {
                if (task != null) {
                    nameEditText.setText(task.name)
                    deadlineEditText.setText(task.deadline)
                    var selection: Int = 0
                    if (task.priority == "Low") {
                        selection = 1
                    } else if (task.priority == "Medium") {
                        selection = 2
                    } else if (task.priority == "High") {
                        selection = 3
                    }
                    prioritySpinner.setSelection(selection)
                    descriptionEditText.setText(task.description)
                }
            }
        }

        editTaskButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val deadline = deadlineEditText.text.toString()
            val priority = prioritySpinner.selectedItem.toString()

            if (name.isEmpty() || description.isEmpty() || deadline.isEmpty() || priority.isEmpty()) {
                Toast.makeText(
                    this@UpdateTaskActivity,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val task = Task(taskId, name, description, priority, deadline)

            CoroutineScope(Dispatchers.IO).launch {
                repository.update(task)
                val data = repository.getAllTasks()
                viewModel.setData(data)
            }
            startActivity(Intent(this@UpdateTaskActivity, MainActivity::class.java))

        }
    }

}