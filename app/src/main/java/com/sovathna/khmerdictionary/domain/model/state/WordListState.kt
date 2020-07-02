package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem

abstract class WordListState : MviState {
  abstract val isInit: Boolean
  abstract val isMore: Boolean
  abstract val words: List<WordItem>?
}