package com.sovathna.khmerdictionary.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.R
import javax.inject.Inject

class WordsAdapter @Inject constructor() :
  ListAdapter<WordItem, RecyclerView.ViewHolder>(WordItem.ITEM_CALLBACK) {

  private var onItemClick: ((Int, WordItem) -> Unit)? = null

  fun setOnItemClickListener(onItemClick: ((Int, WordItem) -> Unit)?) {
    this.onItemClick = onItemClick
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_word, parent, false)
    return WordViewHolder(view, onItemClick)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is WordViewHolder) {
      holder.bindView(getItem(position))
    }
  }
}