package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class DefinitionInteractor :
  MviInteractor<DefinitionIntent, DefinitionResult>() {

  abstract val getDefinition:
      ObservableTransformer<DefinitionIntent.GetDefinition, DefinitionResult>

  abstract val addDeleteBookmark:
      ObservableTransformer<DefinitionIntent.AddDeleteBookmark, DefinitionResult>

  override val intentsProcessor =
    ObservableTransformer<DefinitionIntent, DefinitionResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(DefinitionIntent.GetDefinition::class.java)
            .compose(getDefinition),
          intent
            .ofType(DefinitionIntent.AddDeleteBookmark::class.java)
            .compose(addDeleteBookmark)
        )
      }
    }

}