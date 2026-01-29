package com.miikamenk.todo.model

import java.time.LocalDate

object TaskRepository {
    val mockTasks = listOf(
        Task(1, "kotlin app", "finish the damn app", 2, LocalDate.now().plusDays(1), false),
        Task(2, "get android studio working on linux", "impossible", 5, LocalDate.now().plusDays(3), false),
        Task(3, "blabla", "", 1, LocalDate.now(), true),
        Task(4, "ababa", "...", 3, LocalDate.now().plusDays(2), true),
        Task(5, "some stuff", "what was it again?", 1, LocalDate.now().plusDays(5), false)
    )
}