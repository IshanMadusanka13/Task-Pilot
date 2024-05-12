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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateTaskActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_create_task)

        val repository = TaskRepository(TaskDatabase.getDatabase(this))
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        val nameEditText = findViewById<EditText>(R.id.name)
        val descriptionEditText = findViewById<EditText>(R.id.description)
        val deadlineEditText = findViewById<EditText>(R.id.deadline)
        val prioritySpinner = findViewById<Spinner>(R.id.priority)
        val createTaskButton = findViewById<Button>(R.id.createBtn)

        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

        createTaskButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val deadline = deadlineEditText.text.toString()
            val priority = prioritySpinner.selectedItem.toString()

            if (name.isEmpty() || description.isEmpty() || deadline.isEmpty() || priority.isEmpty()) {
                Toast.makeText(this@CreateTaskActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(0, name, description, priority, deadline)

            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(task)
            }
            startActivity(Intent(this@CreateTaskActivity, MainActivity::class.java))

        }
    }
}