package com.sovathna.khmerdictionary.ui.wordlist.bookmark

import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

class BookmarksFragment :
  WordListFragment<BookmarksIntent, BookmarksState, BookmarksViewModel>() {

  @Inject
  lateinit var getBookmarks: PublishSubject<BookmarksIntent.GetWords>

  override fun intents(): Observable<BookmarksIntent> =
    getBookmarks.cast(BookmarksIntent::class.java)

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
      words?.let {
        rv.post {
          rv.scrollToPosition(0)
        }
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