package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class DefinitionInteractor :
  MviInteractor<DefinitionIntent, DefinitionResult>() {

  abstract val getDefinition:
      ObservableTransformer<DefinitionIntent.Get, DefinitionResult>

  abstract val bookmark:
      ObservableTransformer<DefinitionIntent.Bookmark, DefinitionResult>

  override val intentsProcessor =
    ObservableTransformer<DefinitionIntent, DefinitionResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(DefinitionIntent.Get::class.java)
            .compose(getDefinition),
          intent
            .ofType(DefinitionIntent.Bookmark::class.java)
            .compose(bookmark)
        )
      }
    }

}