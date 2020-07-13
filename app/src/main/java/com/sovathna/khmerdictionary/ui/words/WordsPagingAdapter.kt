package com.sovathna.khmerdictionary.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.model.entity.WordUI

class WordsPagingAdapter :
  PagingDataAdapter<WordItem, WordPagingViewHolder>(WordItem.ITEM_CALLBACK) {
  private var onItemClick: ((Int, WordItem) -> Unit)? = null

  fun setOnItemClickListener(onItemClick: ((Int, WordItem) -> Unit)?) {
    this.onItemClick = onItemClick
  }

  override fun onBindViewHolder(holder: WordPagingViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bindView(it)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordPagingViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_word, parent, false)
    return WordPagingViewHolder(view, onItemClick)
  }
}