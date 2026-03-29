package com.greenmiststudios.zone

import androidx.compose.ui.window.ComposeUIViewController
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase

fun mainViewController() = ComposeUIViewController {
  val driver = NativeSqliteDriver(ZoneDatabase.Schema, "zone.db")
  val repository = SqlDelightTaskRepository(ZoneDatabase(driver))
  App(repository)
}
