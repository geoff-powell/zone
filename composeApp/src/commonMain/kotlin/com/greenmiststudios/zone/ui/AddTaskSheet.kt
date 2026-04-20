package com.greenmiststudios.zone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenmiststudios.zone.Priority
import com.greenmiststudios.zone.Task
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSheet(
  task: Task? = null,
  onDismiss: () -> Unit,
  onSave: (title: String, description: String?, priority: Priority, dueDate: Long?) -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
  val isEditMode = task != null
  var title by remember(task?.id) { mutableStateOf(task?.title ?: "") }
  var description by remember(task?.id) { mutableStateOf(task?.description ?: "") }
  var selectedPriority by remember(task?.id) { mutableStateOf(task?.priority ?: Priority.MEDIUM) }
  var selectedDueDate by remember(task?.id) { mutableStateOf(task?.dueDate) }
  var showDatePicker by remember { mutableStateOf(false) }

  if (showDatePicker) {
    val datePickerState =
      rememberDatePickerState(initialSelectedDateMillis = selectedDueDate)
    DatePickerDialog(
      onDismissRequest = { showDatePicker = false },
      confirmButton = {
        TextButton(
          onClick = {
            selectedDueDate = datePickerState.selectedDateMillis
            showDatePicker = false
          }
        ) {
          Text("OK")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
      },
    ) {
      DatePicker(state = datePickerState)
    }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).imePadding(),
    ) {
      Text(
        text = if (isEditMode) "Edit Task" else "New Task",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Spacer(Modifier.height(20.dp))

      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text("What needs to get done?", color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
      )

      Spacer(Modifier.height(12.dp))

      OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text("Add details (optional)", color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        shape = RoundedCornerShape(12.dp),
        minLines = 2,
        maxLines = 4,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions =
          KeyboardActions(
            onDone = {
              if (title.isNotBlank()) {
                onSave(
                  title.trim(),
                  description.trim().ifBlank { null },
                  selectedPriority,
                  selectedDueDate,
                )
              }
            }
          ),
      )

      Spacer(Modifier.height(20.dp))

      Text(
        text = "Priority",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Spacer(Modifier.height(10.dp))

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Priority.entries.forEach { priority ->
          PriorityChip(
            priority = priority,
            selected = selectedPriority == priority,
            onClick = { selectedPriority = priority },
            modifier = Modifier.weight(1f),
          )
        }
      }

      Spacer(Modifier.height(16.dp))

      DueDateRow(
        dueDate = selectedDueDate,
        onPickDate = { showDatePicker = true },
        onClearDate = { selectedDueDate = null },
      )

      Spacer(Modifier.height(24.dp))

      Button(
        onClick = {
          if (title.isNotBlank()) {
            onSave(
              title.trim(),
              description.trim().ifBlank { null },
              selectedPriority,
              selectedDueDate,
            )
          }
        },
        enabled = title.isNotBlank(),
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
          ),
      ) {
        Text(
          if (isEditMode) "Save Changes" else "Add Task",
          fontWeight = FontWeight.SemiBold,
          fontSize = 16.sp,
        )
      }

      Spacer(Modifier.height(24.dp))
    }
  }
}

@Composable
private fun DueDateRow(dueDate: Long?, onPickDate: () -> Unit, onClearDate: () -> Unit) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    TextButton(
      onClick = onPickDate,
      shape = RoundedCornerShape(10.dp),
    ) {
      Text(
        text = if (dueDate != null) "📅 ${formatDueDateShort(dueDate)}" else "📅 Add due date",
        style = MaterialTheme.typography.bodyMedium,
        color =
          if (dueDate != null) MaterialTheme.colorScheme.primary
          else MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    if (dueDate != null) {
      Spacer(Modifier.width(4.dp))
      TextButton(onClick = onClearDate) {
        Text("✕", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
      }
    }
  }
}

private fun formatDueDateShort(epochMillis: Long): String {
  val tz = TimeZone.currentSystemDefault()
  val date = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz).date
  val today = Clock.System.now().toLocalDateTime(tz).date
  val tomorrow = today.plus(DatePeriod(days = 1))
  return when (date) {
    today -> "Today"
    tomorrow -> "Tomorrow"
    else -> {
      val m = date.month.name
      "${m[0]}${m.substring(1).lowercase()} ${date.dayOfMonth}"
    }
  }
}

@Composable
private fun PriorityChip(
  priority: Priority,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val (color, label) =
    when (priority) {
      Priority.HIGH -> PriorityHigh to "High"
      Priority.MEDIUM -> PriorityMedium to "Medium"
      Priority.LOW -> PriorityLow to "Low"
    }
  val containerColor = if (selected) color.copy(alpha = 0.15f) else Color.Transparent
  val borderColor = if (selected) color else MaterialTheme.colorScheme.outline

  Box(
    modifier =
      modifier
        .clip(RoundedCornerShape(10.dp))
        .background(containerColor)
        .border(width = 1.5.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
        .clickable(onClick = onClick)
        .padding(vertical = 10.dp),
    contentAlignment = Alignment.Center,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
      Spacer(Modifier.width(6.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
