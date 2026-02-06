package com.miikamenk.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miikamenk.todo.model.Task
import com.miikamenk.todo.model.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    private val _visibleTasks = MutableStateFlow<List<Task>>(emptyList())

    val tasks: StateFlow<List<Task>> = _visibleTasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()
    private val _showOnlyDone = MutableStateFlow(false)
    val showOnlyDone: StateFlow<Boolean> = _showOnlyDone.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    init {
        _allTasks.value = TaskRepository.mockTasks
        _visibleTasks.value = _allTasks.value
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
            val nextId = (_allTasks.value.maxOfOrNull { it.id } ?: 0) + 1
            _allTasks.value = _allTasks.value + task.copy(id = nextId)
        }
    }

    fun toggleDone(id: Int) {
        viewModelScope.launch {
            _allTasks.value = _allTasks.value.map { task ->
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
            _allTasks.value = _allTasks.value.filterNot { it.id == id }
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            _allTasks.value = _allTasks.value.map { task ->
                if (task.id == updatedTask.id) {
                    updatedTask
                } else {
                    task
                }
            }
            closeDialog()
        }
    }

    fun showAllTasks() {
        _showOnlyDone.value = false
        _visibleTasks.value = _allTasks.value
    }

    fun filterByDoneState() {
        _showOnlyDone.value = !_showOnlyDone.value
        _visibleTasks.value = _allTasks.value.filter { it.done == _showOnlyDone.value }
    }

    fun sortByDueDate() {
        viewModelScope.launch {
            _allTasks.value = _allTasks.value.sortedBy { it.dueDate }
        }
    }
}
