package com.greenmiststudios.zone

actual class TaskRepositoryFactory {
  actual fun create(): TaskRepository = InMemoryTaskRepository()
}
