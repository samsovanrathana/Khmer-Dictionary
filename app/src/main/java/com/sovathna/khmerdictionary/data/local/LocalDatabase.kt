package com.sovathna.khmerdictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.domain.dao.BookmarkDao
import com.sovathna.khmerdictionary.domain.dao.HistoryDao
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.HistoryEntity

@Database(
  entities = [
    BookmarkEntity::class,
    HistoryEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun historyDao(): HistoryDao
}