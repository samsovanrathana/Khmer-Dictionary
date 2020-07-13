package com.sovathna.khmerdictionary.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.ui.words.WordItem

@Entity(tableName = "bookmarks_ui")
data class BookmarkUI(
  @PrimaryKey
  val id: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  fun toWordItem() = WordItem(id, name, isSelected)
}