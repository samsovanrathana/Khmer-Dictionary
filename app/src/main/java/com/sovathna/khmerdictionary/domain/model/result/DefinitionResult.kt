package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.domain.model.Definition

sealed class DefinitionResult : MviResult {
  data class Success(val definition: Definition) : DefinitionResult()
  data class BookmarkChecked(val isBookmark: Boolean) : DefinitionResult()
  data class HistoryAdded(val id: Long) : DefinitionResult()
}