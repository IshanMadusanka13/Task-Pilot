package com.example.taskpilot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.adapter.TaskAdapter
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayTaskFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_today_task, container, false)
        val context = requireContext()
        val repository = TaskRepository(TaskDatabase.getDatabase(context))
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvToday)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.data.observe(viewLifecycleOwner) {
            adapter = TaskAdapter(it, repository, viewModel, context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getTodayTasks()
            requireActivity().runOnUiThread {
                viewModel.setData(data)
            }
        }

        rootView.findViewById<ImageButton>(R.id.btnSortDeadline).setOnClickListener {
            val txtToday = rootView.findViewById<TextView>(R.id.txtToday)
            val popup = PopupMenu(context, it)
            val inflaterMenu: MenuInflater = popup.menuInflater
            inflaterMenu.inflate(R.menu.deadline_sort_menu, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_Overdue -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = repository.getAllTasks()
                            requireActivity().runOnUiThread {
                                viewModel.getOverDueTasks(data)
                            }
                            txtToday.text = "Overdue"
                        }
                        true
                    }

                    R.id.action_Today -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = repository.getTodayTasks()
                            requireActivity().runOnUiThread {
                                viewModel.setData(data)
                            }
                        }
                        true
                    }

                    R.id.action_Upcoming -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = repository.getAllTasks()
                            requireActivity().runOnUiThread {
                                viewModel.getUpComingTasks(data)
                            }
                            txtToday.text = "Upcoming"
                        }
                        true
                    }

                    else -> false
                }
            }
        }

        return rootView
    }
}