package com.example.taskpilot.ui.views

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskpilot.R
import com.example.taskpilot.ViewTaskActivity
import com.example.taskpilot.database.entities.Task

class TaskViewHolder(view:View, context : Context, tasks : List<Task>):ViewHolder(view) {
    val taskName:TextView
    val priorityView: ImageView
    val taskDeadline: TextView
    val ivDelete:ImageButton
    init {
        taskName = view.findViewById(R.id.taskName)
        priorityView = view.findViewById(R.id.priorityView)
        taskDeadline = view.findViewById(R.id.taskDeadline)
        ivDelete = view.findViewById(R.id.ivDelete)

        view.setOnClickListener {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {

                val clickedTask = tasks[position]
                val intent = Intent(context, ViewTaskActivity::class.java)
                intent.putExtra("taskId", clickedTask.id)
                context.startActivity(intent)
            }
        }

    }
}