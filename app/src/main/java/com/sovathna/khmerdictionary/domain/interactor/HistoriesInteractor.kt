package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<HistoriesIntent, HistoriesResult>() {

  abstract val getWords:
      ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult>

  abstract val update:
      ObservableTransformer<HistoriesIntent.Update, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<HistoriesIntent, HistoriesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(HistoriesIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(HistoriesIntent.Update::class.java)
            .compose(update)
        )
      }
    }
}