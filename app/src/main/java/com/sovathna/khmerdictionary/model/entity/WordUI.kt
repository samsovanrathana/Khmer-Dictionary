package com.sovathna.khmerdictionary.model.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_ui")
data class WordUI(
  @PrimaryKey
  val id: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  companion object {
    val ITEM_CALLBACK = object : DiffUtil.ItemCallback<WordUI>() {
      override fun areItemsTheSame(oldItem: WordUI, newItem: WordUI) =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: WordUI, newItem: WordUI) =
        (oldItem.id == newItem.id) && (oldItem.isSelected == newItem.isSelected)
    }
  }
}