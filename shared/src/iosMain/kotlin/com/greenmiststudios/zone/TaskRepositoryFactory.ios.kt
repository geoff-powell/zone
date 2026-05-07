package com.greenmiststudios.zone

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase

actual class TaskRepositoryFactory {
  actual fun create(): TaskRepository {
    val driver = NativeSqliteDriver(ZoneDatabase.Schema, "zone.db")
    return SqlDelightTaskRepository(ZoneDatabase(driver))
  }
}
