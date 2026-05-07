package com.greenmiststudios.zone

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.greenmiststudios.zone.db.ZoneDatabase
import java.io.File

actual class TaskRepositoryFactory {
  actual fun create(): TaskRepository {
    val dbFile = File(System.getProperty("user.home"), ".zone/zone.db")
    dbFile.parentFile?.mkdirs()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    val currentVersion =
      driver
        .executeQuery(
          identifier = null,
          sql = "PRAGMA user_version",
          mapper = { cursor ->
            if (cursor.next().value) QueryResult.Value(cursor.getLong(0) ?: 0L)
            else QueryResult.Value(0L)
          },
          parameters = 0,
        )
        .value
    val targetVersion = ZoneDatabase.Schema.version
    when {
      currentVersion == 0L -> ZoneDatabase.Schema.create(driver)
      currentVersion < targetVersion ->
        ZoneDatabase.Schema.migrate(driver, currentVersion, targetVersion)
    }
    if (currentVersion != targetVersion) {
      driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
    }
    return SqlDelightTaskRepository(ZoneDatabase(driver))
  }
}
