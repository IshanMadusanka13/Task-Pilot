package com.example.taskpilot.ui.views

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskpilot.R

class TaskViewHolder(view:View):ViewHolder(view) {
    val taskName:TextView
    val taskPriority: TextView
    val taskDeadline: TextView
    val ivDelete:ImageButton
    init {
        taskName = view.findViewById(R.id.taskName)
        taskPriority = view.findViewById(R.id.taskPriority)
        taskDeadline = view.findViewById(R.id.taskDeadline)
        ivDelete = view.findViewById(R.id.ivDelete)
    }
}