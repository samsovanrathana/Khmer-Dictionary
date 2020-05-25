package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import io.reactivex.ObservableTransformer

abstract class MainWordListInteractor : MviInteractor<MainWordListIntent, MainWordListResult>() {

  abstract val getWordList:
      ObservableTransformer<MainWordListIntent.GetWordList, MainWordListResult>

  override val intentsProcessor =
    ObservableTransformer<MainWordListIntent, MainWordListResult> {
      it.publish { intent ->
        intent
          .ofType(MainWordListIntent.GetWordList::class.java)
          .compose(getWordList)
      }
    }
}