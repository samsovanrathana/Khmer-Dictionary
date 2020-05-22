package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class WordListIntent : MviIntent {
  data class GetWords(
    val searchTerm: String = ""
  ) : WordListIntent()

  data class Select(val current: Long?) : WordListIntent()
}