package com.greenmiststudios.zone

data class Task(
  val id: Long,
  val title: String,
  val description: String?,
  val isCompleted: Boolean,
  val priority: Priority,
  val createdAt: Long,
  val dueDate: Long? = null,
  val sortOrder: Long = 0L
)

enum class Priority(val value: Long) {
  HIGH(0L),
  MEDIUM(1L),
  LOW(2L);

  companion object {
    fun fromValue(value: Long): Priority = entries.firstOrNull { it.value == value } ?: MEDIUM
  }
}
