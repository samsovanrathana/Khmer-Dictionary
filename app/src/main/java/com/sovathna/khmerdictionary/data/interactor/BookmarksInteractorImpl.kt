package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.BookmarksInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.BookmarksResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookmarksInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : BookmarksInteractor() {
  override val getWords =
    ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult> {
      it.flatMap { intent ->
        repository
          .getBookmarks(intent.offset, intent.pageSize)
          .subscribeOn(Schedulers.io())
          .map { words ->
            BookmarksResult.Success(
              words,
              words.size >= intent.pageSize
            )
          }
          .subscribeOn(Schedulers.computation())
      }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, BookmarksResult> {
      it.map { intent -> BookmarksResult.SelectWordSuccess }
    }

  override val updateBookmark =
    ObservableTransformer<BookmarksIntent.UpdateBookmark, BookmarksResult> {
      it.map { intent -> BookmarksResult.UpdateBookmarkSuccess(intent.word, intent.isBookmark) }
    }

  override val clearBookmark =
    ObservableTransformer<BookmarksIntent.ClearBookmarks, BookmarksResult> {
      it.flatMap {
        repository
          .clearBookmarks()
          .subscribeOn(Schedulers.io())
          .map { BookmarksResult.ClearBookmarkSuccess }
      }
    }
}