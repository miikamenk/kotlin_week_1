package com.miikamenk.todo.data.repository

import com.miikamenk.todo.data.local.TaskDao
import com.miikamenk.todo.data.model.toEntity
import com.miikamenk.todo.data.model.toTask
import com.miikamenk.todo.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    fun getUndoneTasks(): Flow<List<Task>> {
        return taskDao.getUndoneTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    fun getDoneTasks(): Flow<List<Task>> {
        return taskDao.getDoneTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toTask()
    }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    suspend fun deleteTaskById(id: Int) {
        taskDao.deleteTaskById(id)
    }
}
