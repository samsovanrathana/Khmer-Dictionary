package com.sovathna.khmerdictionary.ui.wordlist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.MainWordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import com.sovathna.khmerdictionary.domain.model.state.MainWordListState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MainWordListViewModel @Inject constructor(
  private val interactor: MainWordListInteractor
) : MviViewModel<MainWordListIntent, MainWordListResult, MainWordListState>() {

  override val reducer =
    BiFunction<MainWordListState, MainWordListResult, MainWordListState> { state, result ->

      when (result) {
        is MainWordListResult.Success ->
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
        is MainWordListResult.Selected -> {
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

  override val stateLiveData: LiveData<MainWordListState> =
    MutableLiveData<MainWordListState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(MainWordListState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}