package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.ui.words.WordItem

data class BookmarksState(
  override val isInit: Boolean = true,
  override val isMore: Boolean = false,
  override val words: List<WordItem>? = null,
  override val loadSuccess: Event<Unit>? = null
) : AbstractWordsState()