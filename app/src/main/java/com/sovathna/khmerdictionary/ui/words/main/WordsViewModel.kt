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
        is WordsResult.SelectWordSuccess -> {
          state.copy(wordsLiveData = state.wordsLiveData?.map {
            it.map { item ->
              if (item.isSelected && item.word.id != result.word?.id) item.copy(isSelected = false)
              else if (item.word.id == result.word?.id) item.copy(isSelected = true)
              else item
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