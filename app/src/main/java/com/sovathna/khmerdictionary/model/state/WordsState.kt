package com.sovathna.khmerdictionary.model.state

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.ui.words.WordItem

data class WordsState(
  override val isInit: Boolean = true,
  override val isMore: Boolean = false,
  override val words: List<WordItem>? = null,
  override val loadSuccess: Event<Unit>? = null,
  val wordsLiveData: LiveData<PagingData<WordItem>>? = null
) : AbstractWordsState()