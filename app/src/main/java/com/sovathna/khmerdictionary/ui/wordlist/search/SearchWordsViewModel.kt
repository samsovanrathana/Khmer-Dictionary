package com.sovathna.khmerdictionary.ui.wordlist.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.SearchWordsInteractor
import com.sovathna.khmerdictionary.domain.model.result.SearchWordsResult
import com.sovathna.khmerdictionary.domain.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SearchWordsViewModel @Inject constructor(
  private val interactor: SearchWordsInteractor
) : MviViewModel<MviIntent, SearchWordsResult, SearchWordsState>() {

  override val reducer =
    BiFunction<SearchWordsState, SearchWordsResult, SearchWordsState> { state, result ->

      when (result) {
        is SearchWordsResult.Success ->
          state.copy(
            isInit = false,
            words =
            if (state.words == null || result.isReset) {
              result.words.map { WordItem(it) }
            } else {
              state.words.toMutableList().apply {
                addAll(result.words.map { WordItem(it) })
              }
            },
            isMore = result.isMore
          )
        is SearchWordsResult.Selected -> {
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

  override val stateLiveData: LiveData<SearchWordsState> =
    MutableLiveData<SearchWordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(SearchWordsState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}