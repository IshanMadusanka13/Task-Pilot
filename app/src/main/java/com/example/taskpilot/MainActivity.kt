package com.example.taskpilot

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.entities.Task
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.adapter.TaskAdapter
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var adapter:TaskAdapter
    private lateinit var viewModel:TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repository = TaskRepository(TaskDatabase.getDatabase(this))
        val recyclerView:RecyclerView = findViewById(R.id.rvTasks)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.data.observe(this){
            adapter = TaskAdapter(it,repository, viewModel)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTasks()
            runOnUiThread {
                viewModel.setData(data)
            }
        }

        findViewById<ImageButton>(R.id.btnSort).setOnClickListener {
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.sort_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_deadline -> {
                        viewModel.sortTasksByDeadline()
                        true
                    }
                    R.id.action_prority -> {
                        viewModel.sortTasksByPriority()
                        true
                    }
                    else -> false
                }
            }
        }



        val btnAddItem: FloatingActionButton = findViewById(R.id.btnAddTask)
        btnAddItem.setOnClickListener {
            createTaskDialog(repository)
        }
}
    fun createTaskDialog(repository: TaskRepository){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Enter New Todo item:")
        builder.setMessage("Enter the todo item below:")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->

            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Task(0,item,"","", Date().toString()))
                val data = repository.getAllTasks()
                runOnUiThread {
                    viewModel.setData(data)
                }
            }
        }
        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}