package com.example.taskpilot.ui.views

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskpilot.R

class TaskViewHolder(view:View):ViewHolder(view) {
    val cbTask:CheckBox
    val ivDelete:ImageButton
    init {
        cbTask = view.findViewById(R.id.cbTaks)
        ivDelete = view.findViewById(R.id.ivDelete)
    }
}