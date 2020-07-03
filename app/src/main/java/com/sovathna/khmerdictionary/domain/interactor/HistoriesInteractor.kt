package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<MviIntent, HistoriesResult>() {

  abstract val getWords:
      ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult>

  abstract val update:
      ObservableTransformer<HistoriesIntent.Update, HistoriesResult>

  abstract val selected:
      ObservableTransformer<MainWordListIntent.Selected, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, HistoriesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(HistoriesIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(HistoriesIntent.Update::class.java)
            .compose(update),
          intent
            .ofType(MainWordListIntent.Selected::class.java)
            .compose(selected)
        )
      }
    }
}