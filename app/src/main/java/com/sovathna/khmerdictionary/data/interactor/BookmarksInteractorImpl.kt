package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.BookmarksInteractor
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.BookmarksResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookmarksInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : BookmarksInteractor() {
  override val getWords =
    ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult> {
      it
        .flatMap { intent ->
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

  override val selected =
    ObservableTransformer<MainWordListIntent.Selected, BookmarksResult> {
      it.map { intent -> BookmarksResult.Selected(intent.word) }
    }
}