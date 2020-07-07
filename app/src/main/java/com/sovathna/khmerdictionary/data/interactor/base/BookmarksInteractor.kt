package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.BookmarksResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class BookmarksInteractor :
  MviInteractor<MviIntent, BookmarksResult>() {

  abstract val getWords:
      ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult>

  abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, BookmarksResult>

  abstract val updateBookmark:
      ObservableTransformer<BookmarksIntent.UpdateBookmark, BookmarksResult>

  abstract val clearBookmark:
      ObservableTransformer<BookmarksIntent.ClearBookmarks, BookmarksResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, BookmarksResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(BookmarksIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord),
          intent
            .ofType(BookmarksIntent.UpdateBookmark::class.java)
            .compose(updateBookmark),
          intent
            .ofType(BookmarksIntent.ClearBookmarks::class.java)
            .compose(clearBookmark)
        )
      }
    }
}