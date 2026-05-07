package com.greenmiststudios.zone

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
  fun getTasks(): Flow<List<Task>>

  suspend fun addTask(
    title: String,
    description: String? = null,
    priority: Priority = Priority.MEDIUM,
    dueDate: Long? = null,
  )

  suspend fun editTask(
    id: Long,
    title: String,
    description: String?,
    priority: Priority,
    dueDate: Long?,
  )

  suspend fun deleteTask(id: Long)

  suspend fun toggleTask(id: Long, isCompleted: Boolean)

  suspend fun updateSortOrder(id: Long, sortOrder: Long)
}
