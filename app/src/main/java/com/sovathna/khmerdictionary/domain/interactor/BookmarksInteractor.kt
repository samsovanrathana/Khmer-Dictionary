package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.result.BookmarksResult
import io.reactivex.ObservableTransformer

abstract class BookmarksInteractor :
  MviInteractor<BookmarksIntent, BookmarksResult>() {

  abstract val getWords:
      ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult>

  override val intentsProcessor =
    ObservableTransformer<BookmarksIntent, BookmarksResult> {
      it.publish { intent ->
        intent
          .ofType(BookmarksIntent.GetWords::class.java)
          .compose(getWords)
      }
    }
}