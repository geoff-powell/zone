package com.greenmiststudios.zone

data class Task(
  val id: Long,
  val title: String,
  val description: String?,
  val isCompleted: Boolean,
  val createdAt: Long,
)
