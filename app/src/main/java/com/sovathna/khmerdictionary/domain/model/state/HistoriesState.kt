package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.khmerdictionary.ui.wordlist.WordItem

data class HistoriesState(
  override val isInit: Boolean = true,
  override val isMore: Boolean = false,
  override val words: List<WordItem>? = null
) : WordListState()