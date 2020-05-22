package com.sovathna.khmerdictionary.ui.wordlist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.MainWordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import com.sovathna.khmerdictionary.domain.model.state.MainWordListState
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MainWordListViewModel @Inject constructor(
  private val interactor: MainWordListInteractor
) : MviViewModel<MainWordListIntent, MainWordListResult, WordListState>() {

  override val reducer =
    BiFunction<WordListState, MainWordListResult, WordListState> { state, result ->
      if (state is MainWordListState) {
        when (result) {
          is MainWordListResult.Success -> state.copy(
            isInit = false,
            words = result.words.map { WordItem(it) },
            isMore = result.isMore
          )
        }
      } else {
        state
      }
    }
  override val stateLiveData: LiveData<WordListState> =
    MutableLiveData<WordListState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(MainWordListState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}