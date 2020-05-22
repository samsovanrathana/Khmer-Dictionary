package com.sovathna.khmerdictionary.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_words")
data class MainWordEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: Long,
  @ColumnInfo(name = "is_select")
  val isSelect: Boolean = false
)