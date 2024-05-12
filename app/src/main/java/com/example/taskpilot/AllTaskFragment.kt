package com.example.taskpilot

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
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

class AllTaskFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_all_task, container, false)
        val context = requireContext()
        val repository = TaskRepository(TaskDatabase.getDatabase(context))
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvTasks)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.data.observe(viewLifecycleOwner) {
            adapter = TaskAdapter(it, repository, viewModel, context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTasks()
            requireActivity().runOnUiThread {
                viewModel.setData(data)
            }
        }

        rootView.findViewById<ImageButton>(R.id.btnSort).setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflaterMenu: MenuInflater = popup.menuInflater
            inflaterMenu.inflate(R.menu.sort_menu, popup.menu)
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

        val btnAddItem: FloatingActionButton = rootView.findViewById(R.id.btnAddTask)
        btnAddItem.setOnClickListener {
            startActivity(Intent(context, CreateTaskActivity::class.java))
        }

        return rootView
    }
}