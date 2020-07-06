package com.sovathna.khmerdictionary.ui.words.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.WordsInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.result.WordsResult
import com.sovathna.khmerdictionary.domain.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class WordsViewModel @ViewModelInject constructor(
  private val interactor: WordsInteractor
) : MviViewModel<WordsIntent, WordsResult, WordsState>() {

  override val reducer =
    BiFunction<WordsState, WordsResult, WordsState> { state, result ->

      when (result) {
        is WordsResult.Success ->
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
        is WordsResult.SelectWordSuccess -> {
          state.copy(words = state.words?.toMutableList()?.apply {
            forEachIndexed { i, v ->
              if (v.isSelected) this[i] = this[i].copy(isSelected = false)
            }
            result.word?.let {
              val index = indexOfFirst { item -> item.word.id == it.id }
              if (index >= 0)
                this[index] = this[index].copy(isSelected = true)
            }
          })
        }
      }
    }

  override val stateLiveData: LiveData<WordsState> =
    MutableLiveData<WordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(WordsState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}