package com.sovathna.khmerdictionary.ui.wordlist.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.BookmarksInteractor
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.result.BookmarksResult
import com.sovathna.khmerdictionary.domain.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class BookmarksViewModel @Inject constructor(
  private val interactor: BookmarksInteractor
) : MviViewModel<BookmarksIntent, BookmarksResult, BookmarksState>() {

  override val reducer =
    BiFunction<BookmarksState, BookmarksResult, BookmarksState> { state, result ->
      when (result) {
        is BookmarksResult.Success ->
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

  override val stateLiveData: LiveData<BookmarksState> =
    MutableLiveData<BookmarksState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(BookmarksState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}