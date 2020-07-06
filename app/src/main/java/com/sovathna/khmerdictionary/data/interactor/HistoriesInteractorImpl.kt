package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.HistoriesInteractor
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.result.HistoriesResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoriesInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : HistoriesInteractor() {
  override val getWords =
    ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult> {
      it
        .flatMap { intent ->
          repository
            .getHistories(intent.offset, intent.pageSize)
            .subscribeOn(Schedulers.io())
            .map { words ->
              HistoriesResult.Success(
                words,
                words.size >= intent.pageSize
              )
            }
        }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, HistoriesResult> {
      it.map { intent -> HistoriesResult.SelectWordSuccess(intent.word) }
    }

  override val clearHistories =
    ObservableTransformer<HistoriesIntent.ClearHistories, HistoriesResult> {
      it.flatMap {
        repository
          .clearHistories()
          .subscribeOn(Schedulers.io())
          .map { HistoriesResult.ClearHistoriesSuccess }
      }
    }
}