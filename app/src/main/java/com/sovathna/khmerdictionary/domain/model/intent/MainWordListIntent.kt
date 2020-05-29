package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.Word

sealed class MainWordListIntent : MviIntent {
  data class GetWordList(
    val offset: Int,
    val pageSize: Int
  ) : MainWordListIntent()

  data class Selected(
    val word: Word?
  ) : MainWordListIntent()
}