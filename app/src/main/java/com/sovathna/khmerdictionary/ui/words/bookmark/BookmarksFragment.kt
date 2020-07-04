package com.sovathna.khmerdictionary.ui.words.bookmark

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BookmarksFragment :
  AbstractWordsFragment<MviIntent, BookmarksState, BookmarksViewModel>() {

  private val getBookmarks = PublishSubject.create<BookmarksIntent.GetWords>()

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getBookmarks,
      selectWordIntent
    )

  override fun render(state: BookmarksState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getBookmarks.onNext(
          BookmarksIntent.GetWords(
            0,
            Const.PAGE_SIZE
          )
        )
      }
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getBookmarks.onNext(
      BookmarksIntent.GetWords(
        offset,
        Const.PAGE_SIZE
      )
    )
  }

}