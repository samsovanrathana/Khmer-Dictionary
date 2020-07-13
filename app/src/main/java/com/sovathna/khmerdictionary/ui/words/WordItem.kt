package com.sovathna.khmerdictionary.ui.words

import androidx.recyclerview.widget.DiffUtil

data class WordItem(
  val id: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  companion object {
    val ITEM_CALLBACK = object : DiffUtil.ItemCallback<WordItem>() {
      override fun areItemsTheSame(oldItem: WordItem, newItem: WordItem) =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: WordItem, newItem: WordItem) =
        oldItem == newItem
    }
  }
}