package com.sovathna.khmerdictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.domain.dao.BookmarkDao
import com.sovathna.khmerdictionary.domain.dao.HistoryDao
import com.sovathna.khmerdictionary.domain.dao.MainWordDao
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.HistoryEntity
import com.sovathna.khmerdictionary.domain.model.MainWordEntity

@Database(
  entities = [
    BookmarkEntity::class,
    HistoryEntity::class,
    MainWordEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun historyDao(): HistoryDao
  abstract fun mainWordDao(): MainWordDao
}