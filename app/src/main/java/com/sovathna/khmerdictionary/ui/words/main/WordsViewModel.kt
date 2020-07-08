package com.sovathna.khmerdictionary.ui.words.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.WordsPagingInteractorImpl
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import com.sovathna.khmerdictionary.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class WordsViewModel @ViewModelInject constructor(
  private val interactor: WordsPagingInteractorImpl
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
        is WordsResult.PagingSuccess ->
          state.copy(
            isInit = false,
            wordsLiveData = result.wordsPager.liveData
              .map { it.map { WordItem(it) } }
              .cachedIn(viewModelScope)
          )
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