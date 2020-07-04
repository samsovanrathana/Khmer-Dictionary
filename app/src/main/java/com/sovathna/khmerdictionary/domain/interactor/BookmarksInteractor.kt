package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.result.BookmarksResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class BookmarksInteractor :
  MviInteractor<MviIntent, BookmarksResult>() {

  abstract val getWords:
      ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult>

  abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, BookmarksResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, BookmarksResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(BookmarksIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord)
        )
      }
    }
}