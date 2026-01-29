package com.miikamenk.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miikamenk.todo.domain.Task
import com.miikamenk.todo.domain.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    init {
        _tasks.value = TaskRepository.mockTasks
    }

    fun selectTask(task: Task) {
        _selectedTask.value = task
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
        _selectedTask.value = null
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val nextId = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1
            _tasks.value = _tasks.value + task.copy(id = nextId)
        }
    }

    fun toggleDone(id: Int) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map { task ->
                if (task.id == id) {
                    task.copy(done = !task.done)
                } else {
                    task
                }
            }
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.filterNot { it.id == id }
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map { task ->
                if (task.id == updatedTask.id) {
                    updatedTask
                } else {
                    task
                }
            }
            closeDialog()
        }
    }

    fun sortByDueDate() {
        viewModelScope.launch {
            _tasks.value = _tasks.value.sortedBy { it.dueDate }
        }
    }
}
