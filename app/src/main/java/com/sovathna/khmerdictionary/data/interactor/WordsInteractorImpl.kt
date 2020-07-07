package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.WordsInteractor
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordsInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : WordsInteractor() {
  override val getWords =
    ObservableTransformer<WordsIntent.GetWords, WordsResult> {
      it.flatMap { intent ->
        repository
          .getWords(intent.offset, intent.pageSize)
          .subscribeOn(Schedulers.io())
          .map { words ->
            WordsResult.Success(words, words.size >= intent.pageSize)
          }.subscribeOn(Schedulers.computation())
      }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, WordsResult> {
      it.map { intent -> WordsResult.SelectWordSuccess(intent.word) }
    }
}