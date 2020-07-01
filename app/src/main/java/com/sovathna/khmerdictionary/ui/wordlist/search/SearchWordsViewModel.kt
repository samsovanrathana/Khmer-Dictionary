package com.sovathna.khmerdictionary.ui.wordlist.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.SearchWordsInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchWordsResult
import com.sovathna.khmerdictionary.domain.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SearchWordsViewModel @Inject constructor(
  private val interactor: SearchWordsInteractor
) : MviViewModel<SearchWordsIntent, SearchWordsResult, SearchWordsState>() {

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
      }
    }

  override val stateLiveData: LiveData<SearchWordsState> =
    MutableLiveData<SearchWordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(SearchWordsState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}