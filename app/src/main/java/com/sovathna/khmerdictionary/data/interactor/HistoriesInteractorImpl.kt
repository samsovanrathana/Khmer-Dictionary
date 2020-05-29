package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.HistoriesInteractor
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
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
            .subscribeOn(Schedulers.computation())
        }
    }

  override val update = ObservableTransformer<HistoriesIntent.Update, HistoriesResult> {
    it.map { intent -> HistoriesResult.Update(intent.word) }
  }
}