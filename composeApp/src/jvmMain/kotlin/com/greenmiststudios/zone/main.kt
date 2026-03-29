package com.greenmiststudios.zone

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase
import java.io.File

fun main() {
  val dbFile = File(System.getProperty("user.home"), ".zone/zone.db")
  dbFile.parentFile?.mkdirs()
  val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
  ZoneDatabase.Schema.create(driver)
  val repository = SqlDelightTaskRepository(ZoneDatabase(driver))

  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Zone",
    ) {
      App(repository)
    }
  }
}
