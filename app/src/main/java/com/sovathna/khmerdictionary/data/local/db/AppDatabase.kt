package com.sovathna.khmerdictionary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.data.local.db.dao.WordDao
import com.sovathna.khmerdictionary.model.entity.WordEntity

@Database(
  entities = [
    WordEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun wordDao(): WordDao
}