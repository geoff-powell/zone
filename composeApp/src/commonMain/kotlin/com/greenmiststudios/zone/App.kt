package com.greenmiststudios.zone

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.greenmiststudios.zone.ui.AddTaskSheet
import com.greenmiststudios.zone.ui.AllDoneScreen
import com.greenmiststudios.zone.ui.FocusScreen
import com.greenmiststudios.zone.ui.HomeScreen
import com.greenmiststudios.zone.ui.ZoneTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

private sealed interface Screen {
  data object Home : Screen
  data object Focus : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(taskRepository: TaskRepository) {
  val tasks by taskRepository.getTasks().collectAsState(initial = emptyList())
  val scope = rememberCoroutineScope()
  var screen by remember { mutableStateOf<Screen>(Screen.Home) }
  var editingTask by remember { mutableStateOf<Task?>(null) }
  var showAddSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val incomplete =
    tasks.filter { !it.isCompleted }.sortedWith(compareBy({ it.priority.value }, { it.sortOrder }))

  ZoneTheme {
    when (screen) {
      is Screen.Home -> {
        HomeScreen(
          tasks = tasks,
          onToggle = { task ->
            scope.launch { taskRepository.toggleTask(task.id, !task.isCompleted) }
          },
          onDelete = { task -> scope.launch { taskRepository.deleteTask(task.id) } },
          onEdit = { task ->
            editingTask = task
            showAddSheet = true
          },
          onAddClick = {
            editingTask = null
            showAddSheet = true
          },
          onFocusClick = { screen = Screen.Focus },
          onMoveUp = { task ->
            scope.launch { moveTask(task, incomplete, up = true, taskRepository) }
          },
          onMoveDown = { task ->
            scope.launch { moveTask(task, incomplete, up = false, taskRepository) }
          },
        )
      }
      is Screen.Focus -> {
        val focusTask = incomplete.firstOrNull()
        if (focusTask == null) {
          AllDoneScreen(onBack = { screen = Screen.Home })
        } else {
          FocusScreen(
            task = focusTask,
            nextTask = incomplete.getOrNull(1),
            onComplete = {
              scope.launch { taskRepository.toggleTask(focusTask.id, true) }
            },
            onSkip = {
              val nextIndex = incomplete.indexOf(focusTask) + 1
              if (nextIndex >= incomplete.size) screen = Screen.Home
            },
            onBack = { screen = Screen.Home },
          )
        }
      }
    }

    if (showAddSheet) {
      val currentEditing = editingTask
      AddTaskSheet(
        task = currentEditing,
        sheetState = sheetState,
        onDismiss = {
          showAddSheet = false
          editingTask = null
        },
        onSave = { title, description, priority, dueDate ->
          scope.launch {
            if (currentEditing != null) {
              taskRepository.editTask(currentEditing.id, title, description, priority, dueDate)
            } else {
              taskRepository.addTask(title, description, priority, dueDate)
            }
          }
          showAddSheet = false
          editingTask = null
        },
      )
    }
  }
}

private suspend fun moveTask(
  task: Task,
  group: List<Task>,
  up: Boolean,
  repository: TaskRepository,
) {
  val priorityGroup = group.filter { it.priority == task.priority }
  val index = priorityGroup.indexOfFirst { it.id == task.id }
  val targetIndex = if (up) index - 1 else index + 1
  if (targetIndex < 0 || targetIndex >= priorityGroup.size) return

  val reordered = priorityGroup.toMutableList().also {
    val tmp = it[index]
    it[index] = it[targetIndex]
    it[targetIndex] = tmp
  }
  reordered.forEachIndexed { i, t -> repository.updateSortOrder(t.id, i.toLong()) }
}

@Preview
@Composable
private fun AppPreview() {
  App(InMemoryTaskRepository())
}
