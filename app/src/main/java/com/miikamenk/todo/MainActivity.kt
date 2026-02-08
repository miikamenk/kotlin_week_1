package com.miikamenk.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.miikamenk.todo.ui.theme.TodoTheme

import com.miikamenk.todo.view.HomeScreen
import com.miikamenk.todo.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TaskViewModel = TaskViewModel()
            val navController = rememberNavController()
            TodoTheme {
                HomeScreen(taskViewModel = TaskViewModel())
                AppNavigation(navController, viewModel)
            }
        }
    }
}