package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<MviIntent, HistoriesResult>() {

  abstract val getWords:
      ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult>

  abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, HistoriesResult>

  abstract val clearHistories:
      ObservableTransformer<HistoriesIntent.ClearHistories, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, HistoriesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(HistoriesIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord),
          intent
            .ofType(HistoriesIntent.ClearHistories::class.java)
            .compose(clearHistories)
        )
      }
    }
}