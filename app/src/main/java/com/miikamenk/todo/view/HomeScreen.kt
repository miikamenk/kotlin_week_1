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
import com.miikamenk.todo.viewmodel.TaskViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel,
    navController: NavHostController
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val showDialog by taskViewModel.showDialog.collectAsState()
    val selectedTask by taskViewModel.selectedTask.collectAsState()
    val showDoneOnly by taskViewModel.showOnlyDone.collectAsState()
    val showAddDialog by taskViewModel.showAddDialog.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("My Tasks") },
            actions = {
                IconButton(onClick = { taskViewModel.openAddDialog() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task"
                    )
                }
                IconButton(onClick = { navController.navigate("calendar") }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Go to calendar"
                    )
                }
                IconButton(onClick = { navController.navigate("weather") }) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = "Go to weather"
                    )
                }
            }
        )

        // Buttons to manipulate tasks
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { taskViewModel.sortByDueDate() }) {
                Text("Sort by Due Date")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { taskViewModel.filterByDoneState() }) {
                Text(if (showDoneOnly) "Show Only Unfinished Tasks" else "Show Only Done Tasks")
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
                    task = selectedTask,
                    taskViewModel = taskViewModel,
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

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { taskViewModel.closeAddDialog() },
            title = { Text("Add New Task") },
            text = {
                DetailDialogContent(
                    task = null,
                    taskViewModel = taskViewModel,
                    onUpdate = taskViewModel::updateTask,
                    onDelete = { }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { taskViewModel.closeAddDialog() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}