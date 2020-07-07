package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment :
  AbstractWordsFragment<MviIntent, BookmarksState, BookmarksViewModel>() {

  override val viewModel: BookmarksViewModel by viewModels()

  private val getBookmarks = PublishSubject.create<BookmarksIntent.GetWords>()

  @Inject
  lateinit var bookmarkMenuItemClickSubject: PublishSubject<BookmarksIntent.UpdateBookmark>

  @Inject
  lateinit var clearBookmarks: PublishSubject<BookmarksIntent.ClearBookmarks>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getBookmarks,
      selectWordIntent,
      clearBookmarks,
      bookmarkMenuItemClickSubject
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
      clearMenuItemLiveData.value = words?.isNotEmpty() == true
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