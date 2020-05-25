package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class MainWordListIntent : MviIntent {
  data class GetWordList(
    val offset: Int,
    val pageSize: Int
  ) : MainWordListIntent()
}