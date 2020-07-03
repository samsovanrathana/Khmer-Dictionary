package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.MainWordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainWordListInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : MainWordListInteractor() {
  override val getWordList =
    ObservableTransformer<MainWordListIntent.GetWordList, MainWordListResult> {
      it.flatMap { intent ->
        repository
          .getMainWordList(intent.offset, intent.pageSize)
          .subscribeOn(Schedulers.io())
          .map { words ->
            MainWordListResult.Success(words, words.size >= intent.pageSize)
          }
          .subscribeOn(Schedulers.computation())
      }
    }

  override val selected =
    ObservableTransformer<MainWordListIntent.Selected, MainWordListResult> {
      it.map { intent -> MainWordListResult.Selected(intent.word) }
    }
}