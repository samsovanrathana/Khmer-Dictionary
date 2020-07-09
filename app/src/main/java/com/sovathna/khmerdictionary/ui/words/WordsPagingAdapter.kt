package com.sovathna.khmerdictionary.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sovathna.khmerdictionary.R

class WordsPagingAdapter :
  PagingDataAdapter<WordItem, WordViewHolder>(WordItem.ITEM_CALLBACK) {
  private var onItemClick: ((Int, WordItem) -> Unit)? = null

  fun setOnItemClickListener(onItemClick: ((Int, WordItem) -> Unit)?) {
    this.onItemClick = onItemClick
  }

  override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bindView(it)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_word, parent, false)
    return WordViewHolder(view, onItemClick)
  }
}