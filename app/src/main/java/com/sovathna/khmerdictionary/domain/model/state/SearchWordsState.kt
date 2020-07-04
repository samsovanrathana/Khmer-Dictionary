package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.khmerdictionary.ui.words.WordItem

data class SearchWordsState(
  override val isInit: Boolean = true,
  override val isMore: Boolean = false,
  override val words: List<WordItem>? = null
) : AbstractWordsState()