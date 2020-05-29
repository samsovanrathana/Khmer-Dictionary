package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.Word

sealed class HistoriesIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : HistoriesIntent()

  data class Update(
    val word: Word
  ) : HistoriesIntent()
}