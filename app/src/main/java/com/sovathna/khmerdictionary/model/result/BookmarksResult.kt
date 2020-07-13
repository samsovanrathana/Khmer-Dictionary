package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.Word

sealed class BookmarksResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean
  ) : BookmarksResult()

  object SelectWordSuccess : BookmarksResult()

  data class UpdateBookmarkSuccess(
    val word: Word,
    val isBookmark: Boolean
  ) : BookmarksResult()

  object ClearBookmarkSuccess : BookmarksResult()
}