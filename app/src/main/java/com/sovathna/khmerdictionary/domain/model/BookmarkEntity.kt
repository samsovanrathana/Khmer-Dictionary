package com.sovathna.khmerdictionary.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark", indices = [Index(value = ["id"], unique = true)])
data class BookmarkEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @ColumnInfo(name = "id")
  val id: Long,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "uid")
  val uid: Long = 0
) {
  fun toWord() = Word(id, word)
}