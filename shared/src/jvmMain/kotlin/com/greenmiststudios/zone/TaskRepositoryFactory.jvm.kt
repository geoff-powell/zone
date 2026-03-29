package com.greenmiststudios.zone

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase
import java.io.File

actual class TaskRepositoryFactory {
  actual fun create(): TaskRepository {
    val dbFile = File(System.getProperty("user.home"), ".zone/zone.db")
    dbFile.parentFile?.mkdirs()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    ZoneDatabase.Schema.create(driver)
    return SqlDelightTaskRepository(ZoneDatabase(driver))
  }
}
