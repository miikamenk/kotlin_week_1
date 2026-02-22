package com.miikamenk.todo.data.model

import com.miikamenk.todo.model.Task
import java.time.LocalDate

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        priority = priority,
        dueDate = LocalDate.ofEpochDay(dueDate),
        done = done
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        priority = priority,
        dueDate = dueDate.toEpochDay(),
        done = done
    )
}
