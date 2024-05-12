package com.example.taskpilot

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.adapter.TaskAdapter
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = TaskRepository(TaskDatabase.getDatabase(this))
        val recyclerView: RecyclerView = findViewById(R.id.rvTasks)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.data.observe(this) {
            adapter = TaskAdapter(it, repository, viewModel, this)
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
            startActivity(Intent(this@MainActivity, CreateTaskActivity::class.java))
        }
    }

}