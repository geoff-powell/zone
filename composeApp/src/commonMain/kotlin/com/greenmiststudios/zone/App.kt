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
  var showAddSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val incomplete = tasks.filter { !it.isCompleted }.sortedWith(compareBy({ it.priority.value }))

  ZoneTheme {
    when (val currentScreen = screen) {
      is Screen.Home -> {
        HomeScreen(
          tasks = tasks,
          onToggle = { task ->
            scope.launch { taskRepository.toggleTask(task.id, !task.isCompleted) }
          },
          onDelete = { task -> scope.launch { taskRepository.deleteTask(task.id) } },
          onAddClick = { showAddSheet = true },
          onFocusClick = { if (incomplete.isNotEmpty()) screen = Screen.Focus },
        )
      }
      is Screen.Focus -> {
        val focusTask = incomplete.firstOrNull()
        if (focusTask == null) {
          screen = Screen.Home
        } else {
          val nextTask = incomplete.getOrNull(1)
          FocusScreen(
            task = focusTask,
            nextTask = nextTask,
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
      AddTaskSheet(
        sheetState = sheetState,
        onDismiss = { showAddSheet = false },
        onAdd = { title, description, priority ->
          scope.launch { taskRepository.addTask(title, description, priority) }
          showAddSheet = false
        },
      )
    }
  }
}

@Preview
@Composable
private fun AppPreview() {
  App(InMemoryTaskRepository())
}
