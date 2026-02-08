package com.miikamenk.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miikamenk.todo.model.Task
import com.miikamenk.todo.model.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TaskFilter { ALL, DONE_ONLY, UNDONE_ONLY }
enum class TaskSort { NONE, BY_DUE_DATE, BY_PRIORITY }

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

    // Filter and sort state
    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()
    
    private val _currentSort = MutableStateFlow(TaskSort.NONE)
    val currentSort: StateFlow<TaskSort> = _currentSort.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    // Form state for new task creation
    private val _newTaskTitle = MutableStateFlow("")
    private val _newTaskDescription = MutableStateFlow("")
    private val _newTaskPriority = MutableStateFlow(3)
    private val _newTaskDueDate = MutableStateFlow(java.time.LocalDate.now())

    val newTaskTitle: StateFlow<String> = _newTaskTitle.asStateFlow()
    val newTaskDescription: StateFlow<String> = _newTaskDescription.asStateFlow()
    val newTaskPriority: StateFlow<Int> = _newTaskPriority.asStateFlow()
    val newTaskDueDate: StateFlow<java.time.LocalDate> = _newTaskDueDate.asStateFlow()

    init {
        _allTasks.value = TaskRepository.mockTasks
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        var filtered = _allTasks.value
        
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
        
        _visibleTasks.value = filtered
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
        // Reset form to defaults
        _newTaskTitle.value = ""
        _newTaskDescription.value = ""
        _newTaskPriority.value = 3
        _newTaskDueDate.value = java.time.LocalDate.now()
    }

    fun closeAddDialog() {
        _showAddDialog.value = false
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val nextId = (_allTasks.value.maxOfOrNull { it.id } ?: 0) + 1
            _allTasks.value = _allTasks.value + task.copy(id = nextId)
            applyFiltersAndSort()
        }
    }

    fun addNewTaskFromForm() {
        if (_newTaskTitle.value.isBlank()) return
        
        val newTask = Task(
            id = 0, // Will be auto-generated
            title = _newTaskTitle.value,
            description = _newTaskDescription.value,
            priority = _newTaskPriority.value,
            dueDate = _newTaskDueDate.value,
            done = false
        )
        
        addTask(newTask)
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

    fun updateNewTaskDueDate(date: java.time.LocalDate) {
        _newTaskDueDate.value = date
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
            applyFiltersAndSort()
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            _allTasks.value = _allTasks.value.filterNot { it.id == id }
            applyFiltersAndSort()
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
            applyFiltersAndSort()
            closeDialog()
        }
    }

    fun showAllTasks() {
        _currentFilter.value = TaskFilter.ALL
        _showOnlyDone.value = false
        applyFiltersAndSort()
    }

    fun filterByDoneState() {
        _showOnlyDone.value = !_showOnlyDone.value
        _currentFilter.value = if (_showOnlyDone.value) {
            TaskFilter.DONE_ONLY
        } else {
            TaskFilter.UNDONE_ONLY
        }
        applyFiltersAndSort()
    }

    fun sortByDueDate() {
        _currentSort.value = if (_currentSort.value == TaskSort.BY_DUE_DATE) {
            TaskSort.NONE
        } else {
            TaskSort.BY_DUE_DATE
        }
        applyFiltersAndSort()
    }

    fun sortByPriority() {
        _currentSort.value = if (_currentSort.value == TaskSort.BY_PRIORITY) {
            TaskSort.NONE
        } else {
            TaskSort.BY_PRIORITY
        }
        applyFiltersAndSort()
    }

    fun resetAllFilters() {
        _currentFilter.value = TaskFilter.ALL
        _currentSort.value = TaskSort.NONE
        _showOnlyDone.value = false
        applyFiltersAndSort()
    }
}
