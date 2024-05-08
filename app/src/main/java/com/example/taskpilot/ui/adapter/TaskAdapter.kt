package com.example.taskpilot.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.taskpilot.R
import com.example.taskpilot.database.entities.Task
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import com.example.taskpilot.ui.views.TaskViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAdapter(Tasks:List<Task>, repository: TaskRepository,
                  viewModel:TaskViewModel):Adapter<TaskViewHolder>() {
    var context: Context? = null
    val tasks = Tasks
    val repository = repository
    val viewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_task_view,parent,false)
        context = parent.context
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.cbTask.text = tasks.get(position).name
        holder.ivDelete.setOnClickListener {
            val isChecked = holder.cbTask.isChecked
            if(isChecked){
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(tasks.get(position))
                    val data = repository.getAllTasks()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                    }
                }
                Toast.makeText(context,"Task Deleted",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Select the Task to delete",Toast.LENGTH_LONG).show()
            }
        }
    }

}