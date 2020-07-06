package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.BookmarksInteractor
import com.sovathna.khmerdictionary.domain.model.result.BookmarksResult
import com.sovathna.khmerdictionary.domain.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.WordItem
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class BookmarksViewModel @Inject constructor(
  private val interactor: BookmarksInteractor
) : MviViewModel<MviIntent, BookmarksResult, BookmarksState>() {

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
        is BookmarksResult.SelectWordSuccess -> {
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
        is BookmarksResult.UpdateBookmarkSuccess -> {
          state.copy(
            words = state.words?.toMutableList()?.apply {
              find { it.word.id == result.word.id }?.let { remove(it) }
              if (result.isBookmark) {
                add(0, WordItem(result.word, true))
              }
            },
            isScrollToTop = if (result.isBookmark) Event(Unit) else null
          )
        }
      }
    }

  override val stateLiveData: LiveData<BookmarksState> =
    MutableLiveData<BookmarksState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(BookmarksState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}