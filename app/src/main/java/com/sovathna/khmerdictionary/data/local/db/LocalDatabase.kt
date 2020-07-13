package com.sovathna.khmerdictionary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.data.local.db.dao.BookmarkDao
import com.sovathna.khmerdictionary.data.local.db.dao.HistoryDao
import com.sovathna.khmerdictionary.data.local.db.dao.WordUIDao
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import com.sovathna.khmerdictionary.model.entity.HistoryEntity
import com.sovathna.khmerdictionary.model.entity.WordUI

@Database(
  entities = [
    BookmarkEntity::class,
    HistoryEntity::class,
    WordUI::class
  ],
  version = 2,
  exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun historyDao(): HistoryDao
  abstract fun wordUIDao(): WordUIDao
}