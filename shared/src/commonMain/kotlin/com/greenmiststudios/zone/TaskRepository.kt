package com.greenmiststudios.zone

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
  fun getTasks(): Flow<List<Task>>

  suspend fun addTask(title: String, description: String? = null)

  suspend fun deleteTask(id: Long)

  suspend fun toggleTask(id: Long, isCompleted: Boolean)
}
