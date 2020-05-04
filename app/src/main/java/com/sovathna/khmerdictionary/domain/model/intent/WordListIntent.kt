package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.FilterType

sealed class WordListIntent : MviIntent {
  data class Filter(
    val filterType: FilterType,
    val searchTerm: String? = null,
    val offset: Int
  ) : WordListIntent()

  data class Select(val current: Long?) : WordListIntent()
}