package com.greenmiststudios.zone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(taskRepository: TaskRepository) {
  MaterialTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background,
    ) {
      TaskScreen(taskRepository)
    }
  }
}

@Composable
private fun TaskScreen(taskRepository: TaskRepository) {
  val tasks by taskRepository.getTasks().collectAsState(initial = emptyList())
  val scope = rememberCoroutineScope()
  var newTaskTitle by remember { mutableStateOf("") }

  Column(
    modifier =
      Modifier
        .safeContentPadding()
        .fillMaxSize()
        .padding(horizontal = 16.dp),
  ) {
    Spacer(Modifier.height(16.dp))
    Text(text = "Zone", fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
    Spacer(Modifier.height(16.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      OutlinedTextField(
        value = newTaskTitle,
        onValueChange = { newTaskTitle = it },
        label = { Text("New task") },
        singleLine = true,
        modifier = Modifier.weight(1f),
      )
      Button(
        onClick = {
          val title = newTaskTitle.trim()
          if (title.isNotEmpty()) {
            scope.launch { taskRepository.addTask(title) }
            newTaskTitle = ""
          }
        },
      ) {
        Text("Add")
      }
    }

    Spacer(Modifier.height(8.dp))
    HorizontalDivider()

    if (tasks.isEmpty()) {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
      ) {
        Text("No tasks yet. Add one above!", color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    } else {
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks, key = { it.id }) { task ->
          TaskItem(
            task = task,
            onToggle = { scope.launch { taskRepository.toggleTask(task.id, !task.isCompleted) } },
            onDelete = { scope.launch { taskRepository.deleteTask(task.id) } },
          )
          HorizontalDivider()
        }
      }
    }
  }
}

@Composable
private fun TaskItem(
  task: Task,
  onToggle: () -> Unit,
  onDelete: () -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
    Text(
      text = task.title,
      modifier = Modifier.weight(1f),
      textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
      color =
        if (task.isCompleted) {
          MaterialTheme.colorScheme.onSurfaceVariant
        } else {
          MaterialTheme.colorScheme.onSurface
        },
    )
    TextButton(onClick = onDelete) {
      Text("Delete", color = MaterialTheme.colorScheme.error)
    }
  }
}

@Preview
@Composable
private fun AppPreview() {
  App(InMemoryTaskRepository())
}
