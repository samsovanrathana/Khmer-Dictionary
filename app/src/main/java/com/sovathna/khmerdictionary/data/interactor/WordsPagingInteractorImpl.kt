package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordsPagingInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : MviInteractor<WordsIntent, WordsResult>() {

  private val getWords =
    ObservableTransformer<WordsIntent.GetWords, WordsResult> {
      it.flatMap { intent ->
        repository
          .getPagingWords(intent.offset)
          .subscribeOn(Schedulers.io())
          .map(WordsResult::PagingSuccess)
      }
    }

  private val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, WordsResult> {
      it.map { intent -> WordsResult.SelectWordSuccess(intent.word) }
    }

  override val intentsProcessor =
    ObservableTransformer<WordsIntent, WordsResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(WordsIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord)
        )

      }
    }
}