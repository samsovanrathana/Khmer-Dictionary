package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class HistoriesIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : HistoriesIntent()
}