package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment :
  AbstractWordsFragment<MviIntent, BookmarksState, BookmarksViewModel>() {

  override val viewModel: BookmarksViewModel by viewModels()

  private val getBookmarksIntent = PublishSubject.create<BookmarksIntent.GetWords>()

  @Inject
  lateinit var clearBookmarksIntent: PublishSubject<BookmarksIntent.ClearBookmarks>

  @Inject
  lateinit var bookmarkMenuItemClickIntent: PublishSubject<BookmarksIntent.UpdateBookmark>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getBookmarksIntent,
      selectWordIntent,
      clearBookmarksIntent,
      bookmarkMenuItemClickIntent
    )

  override fun render(state: BookmarksState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getBookmarksIntent.onNext(
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
    getBookmarksIntent.onNext(
      BookmarksIntent.GetWords(
        offset,
        Const.PAGE_SIZE
      )
    )
  }

}