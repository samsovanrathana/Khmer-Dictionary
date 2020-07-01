package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.Word

sealed class DefinitionIntent : MviIntent {

  data class MenuItemClick(
    val index: Int
  ) : DefinitionIntent()

  data class Get(
    val word: Word
  ) : DefinitionIntent()

  data class Bookmark(
    val word: Word
  ) : DefinitionIntent()
}