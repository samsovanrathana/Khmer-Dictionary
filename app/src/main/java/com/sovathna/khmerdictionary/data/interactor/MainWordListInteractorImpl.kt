package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.MainWordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.result.MainWordListResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class MainWordListInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : MainWordListInteractor() {
  override val getWords = ObservableTransformer<MainWordListIntent.GetWords, MainWordListResult> {
    it.flatMap {
      repository.getWords("")
    }
  }
}