package com.miikamenk.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.miikamenk.todo.ui.theme.TodoTheme

import com.miikamenk.todo.domain.TaskRepository
import com.miikamenk.todo.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoTheme {
                HomeScreen()
            }
        }
    }
}