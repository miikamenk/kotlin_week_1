// File: HomeScreen.kt
package com.miikamenk.todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.miikamenk.todo.domain.Task
import java.time.LocalDate
import com.miikamenk.todo.viewmodel.TaskViewModel
import androidx.compose.material3.OutlinedTextField


@Composable
fun TaskTextField(
    task: String,
    onTaskChange: (String) -> Unit) {
    OutlinedTextField(
        value = task,
        onValueChange = onTaskChange,
        label = { Text("Task") },
    )
}

@Composable
fun DescriptionTextField(
    description: String,
    onDescriptionChange: (String) -> Unit) {
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Description") },
    )
}

@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel
) {
    val tasks by taskViewModel.tasks

    var taskText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "My Tasks",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Buttons to manipulate tasks
        TaskTextField(
            task = taskText,
            onTaskChange = { taskText = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionTextField(
            description = descriptionText,
            onDescriptionChange = { descriptionText = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (taskText.isNotBlank()) {
                    val newTask = Task(
                        id = 0,
                        title = taskText,
                        description = descriptionText,
                        priority = 1,
                        dueDate = LocalDate.now(),
                        done = false
                    )
                    taskViewModel.addTask(newTask)

                    taskText = ""
                    descriptionText = ""
                }
            }) {
                Text("Add Task")
            }

            Button(onClick = { taskViewModel.sortByDueDate() }) {
                Text("Sort by Due Date")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = task.done,
                        onCheckedChange = {
                            taskViewModel.toggleDone(task.id)
                        }
                    )

                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = task.title)
                        Text(text = task.description, fontSize = 12.sp)
                        Text(text = "Due: ${task.dueDate}", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { taskViewModel.removeTask(task.id) }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}