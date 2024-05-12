package com.example.taskpilot

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpilot.database.TaskDatabase
import com.example.taskpilot.database.repositories.TaskRepository
import com.example.taskpilot.ui.adapter.TaskAdapter
import com.example.taskpilot.ui.viewmodels.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = TaskRepository(TaskDatabase.getDatabase(this))

        loadFragment(AllTaskFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.btmNavigate)!!
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_today -> {
                    loadFragment(TodayTaskFragment())
                    true
                }
                R.id.navigation_tasks -> {
                    loadFragment(AllTaskFragment())
                    true
                }
                R.id.navigation_search -> {
                    loadFragment(SearchTaskFragment())
                    true
                }

                else -> false
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}