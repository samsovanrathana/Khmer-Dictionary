package com.sovathna.khmerdictionary.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class WordListViewModel @Inject constructor(
  private val interactor: WordListInteractor
) : MviViewModel<WordListIntent, WordListResult, WordListState>() {

  override val reducer =
    BiFunction<WordListState, WordListResult, WordListState> { state, result ->
      when (result) {
        is WordListResult.Success -> state.copy(
          isInit = false,
          words = if (result.isMore && !result.isReset) state.words?.toMutableList()
            ?.apply { addAll(result.words.map { WordItem(it) }) }
          else result.words.map { WordItem(it, state.selected == it.id) },
          isMore = result.words.size >= Const.PAGE_SIZE,
          resetEvent = if (result.isReset) Event(Unit) else null
        )
        is WordListResult.Select -> {
          state.copy(
            words = state.words?.toMutableList()?.apply {
              find { tmp -> tmp.isSelected }?.let {
                this[indexOf(it)] = it.copy(isSelected = false)
              }
              result.current?.let {
                find { tmp -> tmp.word.id == it }?.let {
                  this[indexOf(it)] = it.copy(isSelected = true)
                }
              }
            },
            selected = result.current
          )
        }
      }
    }

  override val stateLiveData: LiveData<WordListState> =
    MutableLiveData<WordListState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(WordListState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}