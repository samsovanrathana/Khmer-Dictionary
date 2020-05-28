package com.sovathna.khmerdictionary.ui.wordlist.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.HistoriesInteractor
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class HistoriesViewModel @Inject constructor(
  private val interactor: HistoriesInteractor
) : MviViewModel<HistoriesIntent, HistoriesResult, HistoriesState>() {

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
      }
    }

  override val stateLiveData: LiveData<HistoriesState> =
    MutableLiveData<HistoriesState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(HistoriesState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}