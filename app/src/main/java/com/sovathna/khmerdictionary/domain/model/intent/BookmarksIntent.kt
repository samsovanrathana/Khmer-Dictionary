package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.Word

sealed class BookmarksIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : BookmarksIntent()

  data class UpdateBookmark(
    val word: Word,
    val isBookmark: Boolean
  ) : BookmarksIntent()
}