// File: HomeScreen.kt
package com.miikamenk.todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miikamenk.todo.domain.Task
import com.miikamenk.todo.domain.addTask
import com.miikamenk.todo.domain.sortByDueDate
import com.miikamenk.todo.domain.toggleDone
import com.miikamenk.todo.domain.TaskRepository
import java.time.LocalDate

@Composable
fun HomeScreen() {
    var tasks by remember { mutableStateOf(TaskRepository.mockTasks) }

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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val newTask = Task(
                    id = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
                    title = "New Task",
                    description = "A simple task",
                    priority = 1,
                    dueDate = LocalDate.now(),
                    done = false
                )
                tasks = addTask(tasks, newTask)
            }) {
                Text("Add Task")
            }

            Button(onClick = { tasks = sortByDueDate(tasks) }) {
                Text("Sort by Due Date")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        tasks.forEach { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = if (task.done) "X" else "O", modifier = Modifier.padding(end = 8.dp))

                Column {
                    Text(text = task.title)
                    Text(text = task.description, fontSize = 12.sp)
                    Text(text = "Due: ${task.dueDate}", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { tasks = toggleDone(tasks, task.id) }) {
                    Text("Toggle")
                }
            }
        }
    }
}