package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.SearchesInteractor
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.SearchesResult
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchesInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : SearchesInteractor() {
  override val getWords =
    ObservableTransformer<SearchesIntent.GetWords, SearchesResult> {
      it.flatMap { intent ->
        repository
          .getSearches(intent.searchTerm, intent.offset, intent.pageSize)
          .subscribeOn(Schedulers.io())
          .map { words ->
            SearchesResult.Success(
              words,
              words.size >= intent.pageSize,
              intent.isReset
            )
          }
          .subscribeOn(Schedulers.computation())
      }
    }
  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, SearchesResult> {
      it.map { intent -> SearchesResult.SelectWordSuccess(intent.word) }
    }
}