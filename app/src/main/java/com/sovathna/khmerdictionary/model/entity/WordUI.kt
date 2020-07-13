package com.sovathna.khmerdictionary.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.ui.words.WordItem

@Entity(tableName = "words_ui")
data class WordUI(
  @PrimaryKey
  val id: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  fun toWordItem() = WordItem(id, name, isSelected)
}