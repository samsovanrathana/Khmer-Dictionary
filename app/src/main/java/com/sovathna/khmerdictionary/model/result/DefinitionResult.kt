package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.Definition

sealed class DefinitionResult : MviResult {
  data class Success(
    val definition: Definition
  ) : DefinitionResult()

  data class QuickSuccess(
    val definition: Definition
  ) : DefinitionResult()

  data class CheckBookmarkSuccess(
    val isBookmark: Boolean
  ) : DefinitionResult()

  data class BookmarkSuccess(
    val isBookmark: Boolean
  ) : DefinitionResult()

  data class AddHistorySuccess(
    val id: Long
  ) : DefinitionResult()
}