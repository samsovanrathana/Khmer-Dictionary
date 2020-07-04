package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.Word

sealed class WordsIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : WordsIntent()

  data class SelectWord(
    val word: Word?
  ) : WordsIntent()
}