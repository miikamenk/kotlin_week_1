package com.miikamenk.todo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.miikamenk.todo.domain.Task
import com.miikamenk.todo.domain.TaskRepository
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    val tasks = mutableStateOf<List<Task>>(emptyList())

    init {
        tasks.value = TaskRepository.mockTasks
    }

    fun addTask(task: Task) {
        val nextId = (tasks.value.maxOfOrNull { it.id } ?: 0) + 1
        tasks.value = tasks.value + task.copy(id = nextId)
    }

    fun toggleDone(id: Int) {
        tasks.value = tasks.value.map { task ->
            if (task.id == id) {
                task.copy(done = !task.done)
            } else {
                task
            }
        }
    }

    fun removeTask(id: Int) {
        tasks.value = tasks.value.filterNot { it.id == id }
    }

    fun filterByDone(done: Boolean) {
        tasks.value = tasks.value.filter { it.done == done }
    }

    fun sortByDueDate() {
        tasks.value = tasks.value.sortedBy { it.dueDate }
    }
}
