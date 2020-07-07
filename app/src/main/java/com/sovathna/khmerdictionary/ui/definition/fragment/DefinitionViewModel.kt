package com.sovathna.khmerdictionary.ui.definition.fragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.base.DefinitionInteractor
import com.sovathna.khmerdictionary.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.model.result.DefinitionResult
import com.sovathna.khmerdictionary.model.state.DefinitionState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class DefinitionViewModel @ViewModelInject constructor(
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
        is DefinitionResult.QuickSuccess ->
          state.copy(
            quickDef = Event(result.definition)
          )
        is DefinitionResult.CheckBookmarkSuccess -> state.copy(
          isInit = false,
          isBookmark = result.isBookmark
        )
        is DefinitionResult.BookmarkSuccess -> state.copy(
          isBookmark = result.isBookmark,
          isBookmarkEvent = Event(result.isBookmark)
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