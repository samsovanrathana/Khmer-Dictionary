package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<HistoriesIntent, HistoriesResult>() {

  abstract val getWords:
      ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<HistoriesIntent, HistoriesResult> {
      it.publish { intent ->
        intent
          .ofType(HistoriesIntent.GetWords::class.java)
          .compose(getWords)
      }
    }
}