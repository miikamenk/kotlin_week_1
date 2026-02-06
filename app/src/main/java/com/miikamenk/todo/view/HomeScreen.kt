// File: HomeScreen.kt
package com.miikamenk.todo.view

import androidx.compose.foundation.clickable
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
import com.miikamenk.todo.model.Task
import java.time.LocalDate
import com.miikamenk.todo.viewmodel.TaskViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton


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
    val tasks by taskViewModel.tasks.collectAsState()
    val showDialog by taskViewModel.showDialog.collectAsState()
    val selectedTask by taskViewModel.selectedTask.collectAsState()

    var taskText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(1) }

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
                        priority = priority,
                        dueDate = LocalDate.now().plusDays(1),
                        done = false
                    )
                    taskViewModel.addTask(newTask)

                    taskText = ""
                    descriptionText = ""
                    priority = 1 // Lisää tämä
                }
            }) {
                Text("Add Task")
            }

            Button(onClick = { taskViewModel.sortByDueDate() }) {
                Text("Sort by Due Date")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { taskViewModel.filterByDoneState() }) {
                Text("Filter by Done")
            }
            Button(onClick = { taskViewModel.showAllTasks() }) {
                Text("Show all Tasks")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { taskViewModel.selectTask(task) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.done,
                            onCheckedChange = { taskViewModel.toggleDone(task.id) }
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text(text = task.title)
                            Text(text = task.description, fontSize = 12.sp)
                            Text(text = "Due: ${task.dueDate}", fontSize = 12.sp)
                            Text(text = "Priority: ${task.priority}", fontSize = 12.sp)
                        }

                        IconButton(
                            onClick = { taskViewModel.removeTask(task.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
    if (showDialog && selectedTask != null) {
    AlertDialog(
        onDismissRequest = { taskViewModel.closeDialog() },
        title = { Text("Edit Task") },
        text = {
            DetailDialogContent(
                task = selectedTask!!,
                onUpdate = taskViewModel::updateTask,
                onDelete = {
                    taskViewModel.removeTask(selectedTask!!.id)
                    taskViewModel.closeDialog()
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { taskViewModel.closeDialog() }
            ) {
                Text("Close")
            }
        }
    )
    }
}