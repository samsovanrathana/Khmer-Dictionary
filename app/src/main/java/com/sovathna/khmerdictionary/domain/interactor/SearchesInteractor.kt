package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class SearchesInteractor :
  MviInteractor<MviIntent, SearchesResult>() {

  abstract val getWords:
      ObservableTransformer<SearchesIntent.GetWords, SearchesResult>

  abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, SearchesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, SearchesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(SearchesIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord)
        )
      }
    }
}