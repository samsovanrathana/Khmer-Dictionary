package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.Word

sealed class DefinitionIntent : MviIntent {

  data class GetDefinition(
    val word: Word
  ) : DefinitionIntent()

  data class GetQuickDefinition(
    val id: Long
  ) : DefinitionIntent()

  data class AddDeleteBookmark(
    val word: Word
  ) : DefinitionIntent()
}