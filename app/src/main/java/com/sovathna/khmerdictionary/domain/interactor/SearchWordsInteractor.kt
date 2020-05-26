package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchWordsResult
import io.reactivex.ObservableTransformer

abstract class SearchWordsInteractor : MviInteractor<SearchWordsIntent, SearchWordsResult>() {

  abstract val getWords:
      ObservableTransformer<SearchWordsIntent.GetWords, SearchWordsResult>

  override val intentsProcessor =
    ObservableTransformer<SearchWordsIntent, SearchWordsResult> {
      it.publish { intent ->
        intent
          .ofType(SearchWordsIntent.GetWords::class.java)
          .compose(getWords)
      }
    }
}