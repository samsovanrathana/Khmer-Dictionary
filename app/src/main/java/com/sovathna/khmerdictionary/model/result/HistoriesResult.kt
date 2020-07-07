package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.Word

sealed class HistoriesResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean
  ) : HistoriesResult()

  data class SelectWordSuccess(
    val word: Word?
  ) : HistoriesResult()

  object ClearHistoriesSuccess : HistoriesResult()
}