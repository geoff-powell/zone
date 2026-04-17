package com.greenmiststudios.zone

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.greenmiststudios.zone.db.Task as DbTask
import com.greenmiststudios.zone.db.ZoneDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class SqlDelightTaskRepository(database: ZoneDatabase) : TaskRepository {
  private val queries = database.taskQueries

  override fun getTasks(): Flow<List<Task>> =
    queries
      .selectAll()
      .asFlow()
      .mapToList(Dispatchers.Default)
      .map { rows -> rows.map { it.toTask() } }

  override suspend fun addTask(title: String, description: String?, priority: Priority) {
    withContext(Dispatchers.Default) {
      queries.insertTask(
        title = title,
        description = description,
        is_completed = 0L,
        priority = priority.value,
        created_at = Clock.System.now().toEpochMilliseconds(),
      )
    }
  }

  override suspend fun deleteTask(id: Long) {
    withContext(Dispatchers.Default) {
      queries.deleteTask(id)
    }
  }

  override suspend fun toggleTask(id: Long, isCompleted: Boolean) {
    withContext(Dispatchers.Default) {
      queries.updateCompleted(if (isCompleted) 1L else 0L, id)
    }
  }

  private fun DbTask.toTask() =
    Task(
      id = id,
      title = title,
      description = description,
      isCompleted = is_completed != 0L,
      priority = Priority.fromValue(priority),
      createdAt = created_at,
    )
}
