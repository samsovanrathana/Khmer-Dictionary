package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.domain.model.Word

sealed class BookmarksResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean
  ) : BookmarksResult()

  data class SelectWordSuccess(
    val word: Word?
  ) : BookmarksResult()

}