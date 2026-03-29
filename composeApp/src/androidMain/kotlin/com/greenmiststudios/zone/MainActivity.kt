package com.greenmiststudios.zone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val driver = AndroidSqliteDriver(ZoneDatabase.Schema, applicationContext, "zone.db")
    val repository = SqlDelightTaskRepository(ZoneDatabase(driver))
    setContent {
      App(repository)
    }
  }
}

@Preview
@Composable
private fun AppAndroidPreview() {
  App(InMemoryTaskRepository())
}
