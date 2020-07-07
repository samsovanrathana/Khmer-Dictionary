package com.sovathna.khmerdictionary.model.state

import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.ui.words.WordItem

abstract class AbstractWordsState : MviState {
  abstract val isInit: Boolean
  abstract val isMore: Boolean
  abstract val words: List<WordItem>?
  abstract val loadSuccess: Event<Unit>?
}