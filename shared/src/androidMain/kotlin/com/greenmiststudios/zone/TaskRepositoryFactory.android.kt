package com.greenmiststudios.zone

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase

actual class TaskRepositoryFactory(private val context: Context) {
  actual fun create(): TaskRepository {
    val driver = AndroidSqliteDriver(ZoneDatabase.Schema, context, "zone.db")
    return SqlDelightTaskRepository(ZoneDatabase(driver))
  }
}
