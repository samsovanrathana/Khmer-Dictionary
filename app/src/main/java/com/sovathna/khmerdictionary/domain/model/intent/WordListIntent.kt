package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class WordListIntent : MviIntent {
  data class Filter(
    val filter: String?,
    val offset: Int
  ) : WordListIntent()

  data class Select(val current: Long?) : WordListIntent()
}