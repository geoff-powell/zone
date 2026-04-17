package com.greenmiststudios.zone.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenmiststudios.zone.Priority
import com.greenmiststudios.zone.Task

@Composable
fun HomeScreen(
  tasks: List<Task>,
  onToggle: (Task) -> Unit,
  onDelete: (Task) -> Unit,
  onAddClick: () -> Unit,
  onFocusClick: () -> Unit,
) {
  val total = tasks.size
  val done = tasks.count { it.isCompleted }
  val progress = if (total > 0) done.toFloat() / total else 0f

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = onAddClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
      ) {
        Icon(Icons.Filled.Add, contentDescription = "Add task")
      }
    },
    containerColor = MaterialTheme.colorScheme.background,
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().safeContentPadding().padding(innerPadding),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      item {
        HomeHeader(done = done, total = total, progress = progress, onFocusClick = onFocusClick)
        Spacer(Modifier.height(16.dp))
      }

      if (tasks.isEmpty()) {
        item { EmptyState() }
      } else {
        val incomplete = tasks.filter { !it.isCompleted }
        val completed = tasks.filter { it.isCompleted }

        Priority.entries.forEach { priority ->
          val group = incomplete.filter { it.priority == priority }
          if (group.isNotEmpty()) {
            item { PrioritySectionHeader(priority) }
            items(group, key = { it.id }) { task ->
              TaskCard(task = task, onToggle = { onToggle(task) }, onDelete = { onDelete(task) })
            }
            item { Spacer(Modifier.height(4.dp)) }
          }
        }

        if (completed.isNotEmpty()) {
          item {
            Text(
              text = "Completed",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(vertical = 4.dp),
            )
          }
          items(completed, key = { it.id }) { task ->
            TaskCard(task = task, onToggle = { onToggle(task) }, onDelete = { onDelete(task) })
          }
        }
      }
      item { Spacer(Modifier.height(80.dp)) }
    }
  }
}

@Composable
private fun HomeHeader(
  done: Int,
  total: Int,
  progress: Float,
  onFocusClick: () -> Unit,
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column {
        Text(
          text = "Zone",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = if (total == 0) "All clear! Add a task." else "$done of $total tasks done",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      if (total > done) {
        Button(
          onClick = onFocusClick,
          colors =
            ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.primary,
            ),
          shape = RoundedCornerShape(12.dp),
        ) {
          Text("⚡ Focus", fontWeight = FontWeight.SemiBold)
        }
      }
    }

    if (total > 0) {
      Spacer(Modifier.height(12.dp))
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round,
      )
    }
  }
}

@Composable
private fun PrioritySectionHeader(priority: Priority) {
  val (color, label) =
    when (priority) {
      Priority.HIGH -> PriorityHigh to "High Priority"
      Priority.MEDIUM -> PriorityMedium to "Medium"
      Priority.LOW -> PriorityLow to "Low"
    }
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(vertical = 4.dp),
  ) {
    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
    Spacer(Modifier.width(8.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.SemiBold,
      color = color,
    )
  }
}

@Composable
fun TaskCard(
  task: Task,
  onToggle: () -> Unit,
  onDelete: () -> Unit,
) {
  val priorityColor =
    when {
      task.isCompleted -> PriorityCompleted
      task.priority == Priority.HIGH -> PriorityHigh
      task.priority == Priority.MEDIUM -> PriorityMedium
      else -> PriorityLow
    }
  val containerColor by
    animateColorAsState(
      targetValue =
        when {
          task.isCompleted -> PriorityCompletedContainer
          task.priority == Priority.HIGH -> PriorityHighContainer
          task.priority == Priority.MEDIUM -> PriorityMediumContainer
          else -> PriorityLowContainer
        },
      animationSpec = tween(300),
    )

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = containerColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier = Modifier.size(4.dp).clip(CircleShape).background(priorityColor),
      )
      Spacer(Modifier.width(4.dp))
      Checkbox(
        checked = task.isCompleted,
        onCheckedChange = { onToggle() },
        colors =
          CheckboxDefaults.colors(
            checkedColor = priorityColor,
            uncheckedColor = priorityColor,
          ),
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = if (!task.isCompleted) FontWeight.Medium else FontWeight.Normal,
          textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
          color =
            if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface,
        )
        if (!task.description.isNullOrBlank()) {
          Text(
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
          )
        }
      }
      TextButton(onClick = onDelete, contentPadding = PaddingValues(4.dp)) {
        Text("✕", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
      }
    }
  }
}

@Composable
private fun EmptyState() {
  Column(
    modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text("🎉", fontSize = 48.sp)
    Spacer(Modifier.height(12.dp))
    Text(
      text = "Brain is clear!",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Text(
      text = "Tap + to add your first task",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
