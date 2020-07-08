package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.model.result.DefinitionResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class DefinitionInteractor :
  MviInteractor<DefinitionIntent, DefinitionResult>() {

  protected abstract val getDefinition:
      ObservableTransformer<DefinitionIntent.GetDefinition, DefinitionResult>

  protected abstract val addDeleteBookmark:
      ObservableTransformer<DefinitionIntent.AddDeleteBookmark, DefinitionResult>

  protected abstract val getQuickDefinition:
      ObservableTransformer<DefinitionIntent.GetQuickDefinition, DefinitionResult>

  override val intentsProcessor =
    ObservableTransformer<DefinitionIntent, DefinitionResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(DefinitionIntent.GetDefinition::class.java)
            .compose(getDefinition),
          intent
            .ofType(DefinitionIntent.AddDeleteBookmark::class.java)
            .compose(addDeleteBookmark),
          intent
            .ofType(DefinitionIntent.GetQuickDefinition::class.java)
            .compose(getQuickDefinition)
        )
      }
    }

}