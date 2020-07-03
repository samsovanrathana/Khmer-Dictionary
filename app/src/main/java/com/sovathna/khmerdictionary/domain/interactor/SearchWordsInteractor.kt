package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchWordsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class SearchWordsInteractor :
  MviInteractor<MviIntent, SearchWordsResult>() {

  abstract val getWords:
      ObservableTransformer<SearchWordsIntent.GetWords, SearchWordsResult>

  abstract val selected:
      ObservableTransformer<MainWordListIntent.Selected, SearchWordsResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, SearchWordsResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(SearchWordsIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(MainWordListIntent.Selected::class.java)
            .compose(selected)
        )
      }
    }
}