package com.sovathna.khmerdictionary.ui.words.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.HistoriesInteractor
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class HistoriesViewModel @Inject constructor(
  private val interactor: HistoriesInteractor
) : MviViewModel<MviIntent, HistoriesResult, HistoriesState>() {

  override val reducer =
    BiFunction<HistoriesState, HistoriesResult, HistoriesState> { state, result ->
      when (result) {
        is HistoriesResult.Success ->
          state.copy(
            isInit = false,
            words =
            if (state.words == null) {
              result.words.map { WordItem(it) }
            } else {
              state.words.toMutableList().apply {
                addAll(result.words.map { WordItem(it) })
              }
            },
            isMore = result.isMore
          )
        is HistoriesResult.SelectWordSuccess -> {
          state.copy(words = state.words?.toMutableList()?.apply {
            if (isNotEmpty()) {
              forEachIndexed { i, v ->
                if (v.isSelected) this[i] = this[i].copy(isSelected = false)
              }
              result.word?.let { word ->

                val index = indexOfFirst { item -> item.word.id == word.id }
                if (index > 0) {
                  val tmp = this.removeAt(index)
                  add(0, tmp)
                }
                this[0] = this[0].copy(isSelected = true)
              }
            }
          })
        }
        is HistoriesResult.ClearHistoriesSuccess ->
          state.copy(
            words = emptyList()
          )
      }
    }

  override val stateLiveData: LiveData<HistoriesState> =
    MutableLiveData<HistoriesState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(HistoriesState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}