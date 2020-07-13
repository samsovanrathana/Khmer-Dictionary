package com.sovathna.khmerdictionary.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.ui.words.WordItem

@Entity(tableName = "histories_ui", indices = [Index(value = ["uid"], unique = true)])
data class HistoryUI(
  @PrimaryKey
  val id: Long,
  val uid: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  fun toWordItem() = WordItem(id, name, isSelected)
}