package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class SearchWordsIntent : MviIntent {
  data class GetWords(
    val searchTerm: String,
    val offset: Int,
    val pageSize: Int,
    val isReset: Boolean
  ) : SearchWordsIntent()
}