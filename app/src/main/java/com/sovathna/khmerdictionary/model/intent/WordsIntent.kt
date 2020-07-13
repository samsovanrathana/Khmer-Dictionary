package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class WordsIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : WordsIntent()

  data class SelectWord(
    val id: Long?
  ) : WordsIntent()
}