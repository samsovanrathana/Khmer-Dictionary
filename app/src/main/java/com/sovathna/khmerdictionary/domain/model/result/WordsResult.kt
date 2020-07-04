package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.domain.model.Word

sealed class WordsResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean
  ) : WordsResult()

  data class SelectWordSuccess(
    val word: Word?
  ) : WordsResult()
}