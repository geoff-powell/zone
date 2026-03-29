package com.greenmiststudios.zone

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

class InMemoryTaskRepository : TaskRepository {
  private val _tasks = MutableStateFlow<List<Task>>(emptyList())
  private var nextId = 1L

  override fun getTasks(): Flow<List<Task>> = _tasks.asStateFlow()

  override suspend fun addTask(title: String, description: String?) {
    _tasks.update { current ->
      current +
        Task(
          id = nextId++,
          title = title,
          description = description,
          isCompleted = false,
          createdAt = Clock.System.now().toEpochMilliseconds(),
        )
    }
  }

  override suspend fun deleteTask(id: Long) {
    _tasks.update { current -> current.filter { it.id != id } }
  }

  override suspend fun toggleTask(id: Long, isCompleted: Boolean) {
    _tasks.update { current ->
      current.map { task -> if (task.id == id) task.copy(isCompleted = isCompleted) else task }
    }
  }
}
