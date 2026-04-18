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

  override suspend fun addTask(
    title: String,
    description: String?,
    priority: Priority,
    dueDate: Long?,
  ) {
    _tasks.update { current ->
      val sortOrder = current.count { it.priority == priority && !it.isCompleted }.toLong()
      (current +
          Task(
            id = nextId++,
            title = title,
            description = description,
            isCompleted = false,
            priority = priority,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            dueDate = dueDate,
            sortOrder = sortOrder,
          ))
        .taskOrder()
    }
  }

  override suspend fun editTask(
    id: Long,
    title: String,
    description: String?,
    priority: Priority,
    dueDate: Long?,
  ) {
    _tasks.update { current ->
      current
        .map { task ->
          if (task.id == id)
            task.copy(
              title = title,
              description = description,
              priority = priority,
              dueDate = dueDate,
            )
          else task
        }
        .taskOrder()
    }
  }

  override suspend fun deleteTask(id: Long) {
    _tasks.update { current -> current.filter { it.id != id } }
  }

  override suspend fun toggleTask(id: Long, isCompleted: Boolean) {
    _tasks.update { current ->
      current
        .map { task -> if (task.id == id) task.copy(isCompleted = isCompleted) else task }
        .taskOrder()
    }
  }

  override suspend fun updateSortOrder(id: Long, sortOrder: Long) {
    _tasks.update { current ->
      current.map { task -> if (task.id == id) task.copy(sortOrder = sortOrder) else task }
    }
  }

  private fun List<Task>.taskOrder() =
    sortedWith(
      compareBy({ it.isCompleted }, { it.priority.value }, { it.sortOrder }, { it.createdAt })
    )
}
