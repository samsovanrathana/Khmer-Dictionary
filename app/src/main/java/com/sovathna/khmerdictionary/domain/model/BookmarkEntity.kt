package com.sovathna.khmerdictionary.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark", indices = [Index(value = ["word_id"], unique = true)])
data class BookmarkEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @ColumnInfo(name = "word_id")
  val wordId: Long,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0
)