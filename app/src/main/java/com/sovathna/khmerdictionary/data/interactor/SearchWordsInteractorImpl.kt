package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.SearchWordsInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.result.SearchWordsResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchWordsInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : SearchWordsInteractor() {
  override val getWords =
    ObservableTransformer<SearchWordsIntent.GetWords, SearchWordsResult> {
      it
        .flatMap { intent ->
          repository
            .getSearchWords(intent.searchTerm, intent.offset, intent.pageSize)
            .subscribeOn(Schedulers.io())
            .map { words ->
              SearchWordsResult.Success(
                words,
                words.size >= intent.pageSize,
                intent.isReset
              )
            }
            .subscribeOn(Schedulers.computation())
        }
    }
  override val selected =
    ObservableTransformer<MainWordListIntent.Selected, SearchWordsResult> {
      it.map { intent -> SearchWordsResult.Selected(intent.word) }
    }
}