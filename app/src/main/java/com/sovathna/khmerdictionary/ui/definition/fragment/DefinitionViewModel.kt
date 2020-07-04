package com.sovathna.khmerdictionary.ui.definition.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import com.sovathna.khmerdictionary.domain.model.state.DefinitionState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class DefinitionViewModel @Inject constructor(
  private val interactor: DefinitionInteractor
) : MviViewModel<DefinitionIntent, DefinitionResult, DefinitionState>() {

  override val reducer =
    BiFunction<DefinitionState, DefinitionResult, DefinitionState> { state, result ->
      when (result) {
        is DefinitionResult.Success ->
          state.copy(
            isInit = false,
            definition = result.definition
          )
        is DefinitionResult.BookmarkSuccess -> state.copy(
          isInit = false,
          isBookmark = result.isBookmark
        )
        is DefinitionResult.AddHistorySuccess -> state
      }
    }
  override val stateLiveData: LiveData<DefinitionState> =
    MutableLiveData<DefinitionState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(DefinitionState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}