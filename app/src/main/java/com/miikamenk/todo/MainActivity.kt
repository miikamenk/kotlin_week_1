package com.miikamenk.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.miikamenk.todo.data.local.AppDatabase
import com.miikamenk.todo.data.repository.TaskRepository
import com.miikamenk.todo.navigation.AppNavigation
import com.miikamenk.todo.ui.theme.TodoTheme
import com.miikamenk.todo.viewmodel.TaskViewModel
import com.miikamenk.todo.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val taskRepository = TaskRepository(database.taskDao())

        setContent {
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModel.Factory(taskRepository)
            )
            val weatherViewModel: WeatherViewModel = WeatherViewModel()
            val navController = rememberNavController()
            TodoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController, taskViewModel, weatherViewModel)
                }
            }
        }
    }
}