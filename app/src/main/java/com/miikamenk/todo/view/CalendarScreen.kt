package com.miikamenk.todo.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.ui.Alignment
import com.miikamenk.todo.viewmodel.TaskViewModel
import androidx.navigation.NavHostController
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    taskViewModel: TaskViewModel,
    navController: NavHostController
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val showDialog by taskViewModel.showDialog.collectAsState()
    val selectedTask by taskViewModel.selectedTask.collectAsState()
    val showAddDialog by taskViewModel.showAddDialog.collectAsState()

    // Group tasks by due date
    val tasksByDate = tasks.groupBy { it.dueDate }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Calendar") },
            actions = {
                IconButton(onClick = { taskViewModel.openAddDialog() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task"
                    )
                }
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Task List"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (tasksByDate.isEmpty()) {
                item {
                    Text(
                        text = "No tasks found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                tasksByDate.forEach { (date, dateTasks) ->
                    item {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(dateTasks) { task ->
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

    // Add task dialog
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
