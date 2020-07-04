package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.SearchesInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchesResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchesInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : SearchesInteractor() {
  override val getWords =
    ObservableTransformer<SearchesIntent.GetWords, SearchesResult> {
      it
        .flatMap { intent ->
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