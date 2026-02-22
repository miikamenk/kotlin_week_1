package com.miikamenk.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miikamenk.todo.data.repository.TaskRepository
import com.miikamenk.todo.model.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class TaskFilter { ALL, DONE_ONLY, UNDONE_ONLY }
enum class TaskSort { NONE, BY_DUE_DATE, BY_PRIORITY }

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()

    private val _currentSort = MutableStateFlow(TaskSort.NONE)
    val currentSort: StateFlow<TaskSort> = _currentSort.asStateFlow()

    private val _allTasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filteredAndSortedTasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _filteredAndSortedTasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _showOnlyDone = MutableStateFlow(false)
    val showOnlyDone: StateFlow<Boolean> = _showOnlyDone.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    private val _newTaskTitle = MutableStateFlow("")
    private val _newTaskDescription = MutableStateFlow("")
    private val _newTaskPriority = MutableStateFlow(3)
    private val _newTaskDueDate = MutableStateFlow(LocalDate.now())

    val newTaskTitle: StateFlow<String> = _newTaskTitle.asStateFlow()
    val newTaskDescription: StateFlow<String> = _newTaskDescription.asStateFlow()
    val newTaskPriority: StateFlow<Int> = _newTaskPriority.asStateFlow()
    val newTaskDueDate: StateFlow<LocalDate> = _newTaskDueDate.asStateFlow()

    init {
        viewModelScope.launch {
            _allTasks.collect { taskList ->
                applyFiltersAndSort(taskList)
            }
        }
    }

    private fun applyFiltersAndSort(taskList: List<Task>) {
        var filtered = taskList

        when (_currentFilter.value) {
            TaskFilter.DONE_ONLY -> filtered = filtered.filter { it.done }
            TaskFilter.UNDONE_ONLY -> filtered = filtered.filter { !it.done }
            TaskFilter.ALL -> { /* no filter */ }
        }

        filtered = when (_currentSort.value) {
            TaskSort.BY_DUE_DATE -> filtered.sortedBy { it.dueDate }
            TaskSort.BY_PRIORITY -> filtered.sortedByDescending { it.priority }
            TaskSort.NONE -> filtered
        }

        _filteredAndSortedTasks.value = filtered
    }

    fun selectTask(task: Task) {
        _selectedTask.value = task
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
        _selectedTask.value = null
    }

    fun openAddDialog() {
        _showAddDialog.value = true
        _newTaskTitle.value = ""
        _newTaskDescription.value = ""
        _newTaskPriority.value = 3
        _newTaskDueDate.value = LocalDate.now()
    }

    fun closeAddDialog() {
        _showAddDialog.value = false
    }

    fun addNewTaskFromForm() {
        if (_newTaskTitle.value.isBlank()) return

        val newTask = Task(
            id = 0,
            title = _newTaskTitle.value,
            description = _newTaskDescription.value,
            priority = _newTaskPriority.value,
            dueDate = _newTaskDueDate.value,
            done = false
        )

        viewModelScope.launch {
            repository.insertTask(newTask)
        }
        closeAddDialog()
    }

    fun updateNewTaskTitle(title: String) {
        _newTaskTitle.value = title
    }

    fun updateNewTaskDescription(description: String) {
        _newTaskDescription.value = description
    }

    fun updateNewTaskPriority(priority: Int) {
        _newTaskPriority.value = priority
    }

    fun updateNewTaskDueDate(date: LocalDate) {
        _newTaskDueDate.value = date
    }

    fun toggleDone(id: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(id)
            task?.let {
                repository.updateTask(it.copy(done = !it.done))
            }
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask)
            closeDialog()
        }
    }

    fun showAllTasks() {
        _currentFilter.value = TaskFilter.ALL
        _showOnlyDone.value = false
        applyFiltersAndSort(_allTasks.value)
    }

    fun filterByDoneState() {
        _showOnlyDone.value = !_showOnlyDone.value
        _currentFilter.value = if (_showOnlyDone.value) {
            TaskFilter.DONE_ONLY
        } else {
            TaskFilter.UNDONE_ONLY
        }
        applyFiltersAndSort(_allTasks.value)
    }

    fun sortByDueDate() {
        _currentSort.value = if (_currentSort.value == TaskSort.BY_DUE_DATE) {
            TaskSort.NONE
        } else {
            TaskSort.BY_DUE_DATE
        }
        applyFiltersAndSort(_allTasks.value)
    }

    fun sortByPriority() {
        _currentSort.value = if (_currentSort.value == TaskSort.BY_PRIORITY) {
            TaskSort.NONE
        } else {
            TaskSort.BY_PRIORITY
        }
        applyFiltersAndSort(_allTasks.value)
    }

    fun resetAllFilters() {
        _currentFilter.value = TaskFilter.ALL
        _currentSort.value = TaskSort.NONE
        _showOnlyDone.value = false
        applyFiltersAndSort(_allTasks.value)
    }

    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
