package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class MainWordListInteractor : MviInteractor<MainWordListIntent, MainWordListResult>() {

  abstract val getWordList:
      ObservableTransformer<MainWordListIntent.GetWordList, MainWordListResult>

  abstract val selected:
      ObservableTransformer<MainWordListIntent.Selected, MainWordListResult>

  override val intentsProcessor =
    ObservableTransformer<MainWordListIntent, MainWordListResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(MainWordListIntent.GetWordList::class.java)
            .compose(getWordList),
          intent
            .ofType(MainWordListIntent.Selected::class.java)
            .compose(selected)
        )

      }
    }
}