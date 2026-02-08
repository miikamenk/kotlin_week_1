package com.miikamenk.todo.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miikamenk.todo.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DetailDialogContent(
    task: Task?,
    taskViewModel: com.miikamenk.todo.viewmodel.TaskViewModel,
    onUpdate: (Task) -> Unit,
    onDelete: () -> Unit
) {
    val isNewTask = task == null
    
    var title by remember {
        mutableStateOf(if (isNewTask) "" else task!!.title) 
    }
    var description by remember { 
        mutableStateOf(if (isNewTask) "" else task!!.description) 
    }
    var priority by remember { 
        mutableStateOf(if (isNewTask) "3" else task!!.priority.toString()) 
    }
    var dueDate by remember { 
        mutableStateOf(if (isNewTask) LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) 
                     else task!!.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)) 
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = priority,
            onValueChange = { priority = it },
            label = { Text("Priority (1-5)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            label = { Text("Due Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // only show delete button for existing tasks
            if (!isNewTask) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete Task")
                }
            }

            Button(
                onClick = {
                    if (isNewTask) {
                        // For new task, update ViewModel state and call add
                        taskViewModel.updateNewTaskTitle(title)
                        taskViewModel.updateNewTaskDescription(description)
                        taskViewModel.updateNewTaskPriority(priority.toIntOrNull() ?: 3)
                        taskViewModel.updateNewTaskDueDate(LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE))
                        taskViewModel.addNewTaskFromForm()
                    } else {
                        // For existing task, update it
                        val updatedTask = task!!.copy(
                            title = title,
                            description = description,
                            priority = priority.toIntOrNull() ?: 1,
                            dueDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        )
                        onUpdate(updatedTask)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isNewTask) "Add Task" else "Save Changes")
            }
        }
    }
}
