package com.greenmiststudios.zone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowBack
import androidx.compose.material3.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenmiststudios.zone.Priority
import com.greenmiststudios.zone.Task

@Composable
fun AllDoneScreen(onBack: () -> Unit) {
  Column(
    modifier =
      Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .safeContentPadding()
        .padding(horizontal = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text("🎉", fontSize = 72.sp)
    Spacer(Modifier.height(24.dp))
    Text(
      text = "All done!",
      fontSize = 32.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = "You crushed it. Time for a break.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(40.dp))
    Button(
      onClick = onBack,
      shape = RoundedCornerShape(14.dp),
      colors =
        ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
      Text("Back to Home", fontWeight = FontWeight.SemiBold)
    }
  }
}

@Composable
fun FocusScreen(
  task: Task,
  nextTask: Task?,
  onComplete: () -> Unit,
  onSkip: () -> Unit,
  onBack: () -> Unit
) {
  val priorityColor =
    when (task.priority) {
      Priority.HIGH -> PriorityHigh
      Priority.MEDIUM -> PriorityMedium
      Priority.LOW -> PriorityLow
    }
  val priorityLabel =
    when (task.priority) {
      Priority.HIGH -> "High Priority"
      Priority.MEDIUM -> "Medium Priority"
      Priority.LOW -> "Low Priority"
    }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .safeContentPadding()
        .padding(horizontal = 24.dp)
  ) {
    Spacer(Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onBack) {
        Icon(
          Icons.Filled.ArrowBack,
          contentDescription = "Back",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Text(
        text = "Focus Mode",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    Spacer(Modifier.height(32.dp))

    Text(
      text = "Right now",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(4.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors =
        CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(priorityColor))
          Spacer(Modifier.width(8.dp))
          Text(
            text = priorityLabel,
            style = MaterialTheme.typography.labelMedium,
            color = priorityColor,
            fontWeight = FontWeight.SemiBold
          )
        }
        Spacer(Modifier.height(16.dp))
        Text(
          text = task.title,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 32.sp
        )
        if (!task.description.isNullOrBlank()) {
          Spacer(Modifier.height(12.dp))
          Text(
            text = task.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(Modifier.height(32.dp))

    Button(
      onClick = onComplete,
      modifier = Modifier.fillMaxWidth().height(56.dp),
      shape = RoundedCornerShape(16.dp),
      colors =
        ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
      Icon(Icons.Filled.Check, contentDescription = null)
      Spacer(Modifier.width(8.dp))
      Text("Done", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }

    Spacer(Modifier.height(12.dp))

    OutlinedButton(
      onClick = onSkip,
      modifier = Modifier.fillMaxWidth().height(56.dp),
      shape = RoundedCornerShape(16.dp)
    ) {
      Text(
        "Skip for now",
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    if (nextTask != null) {
      Spacer(Modifier.height(40.dp))
      Text(
        text = "Up next",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors =
          CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Text(
          text = nextTask.title,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
          maxLines = 2
        )
      }
    } else {
      Spacer(Modifier.height(40.dp))
      Text(
        text = "Last task — finish strong!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
