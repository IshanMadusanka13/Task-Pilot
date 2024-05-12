package com.example.taskpilot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

class SearchTaskFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search_task, container, false)
        val context = requireContext()
        val repository = TaskRepository(TaskDatabase.getDatabase(context))
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvSearch)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.data.observe(viewLifecycleOwner) {
            adapter = TaskAdapter(it, repository, viewModel, context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        val txtSearch = rootView.findViewById<EditText>(R.id.txtSearch)
        txtSearch.setOnKeyListener { v, keyCode, event ->
            if (txtSearch.text.toString() != "Find By Task Name") {
                CoroutineScope(Dispatchers.IO).launch {
                    val data = repository.getTaskByName(txtSearch.text.toString())
                    requireActivity().runOnUiThread {
                        viewModel.setData(data)
                    }
                }
            }else{
                txtSearch.setText("")
            }
            return@setOnKeyListener false
        }
        return rootView
    }

}