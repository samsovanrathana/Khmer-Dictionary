package com.sovathna.khmerdictionary.model.result

import androidx.paging.Pager
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.Word

sealed class WordsResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean
  ) : WordsResult()

  data class PagingSuccess(
    val wordsPager: Pager<Int, Word>
  ) : WordsResult()

  data class SelectWordSuccess(
    val word: Word?
  ) : WordsResult()
}