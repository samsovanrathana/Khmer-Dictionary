package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordsInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : MviInteractor<WordsIntent, WordsResult>() {

  private val getWords =
    ObservableTransformer<WordsIntent.GetWords, WordsResult> {
      it.flatMap {
        repository
          .getWordsPager()
          .subscribeOn(Schedulers.io())
          .map(WordsResult::PagingSuccess)
      }
    }

  private val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, WordsResult> {
      it.flatMap { intent ->
        repository
          .selectWord(intent.id)
          .subscribeOn(Schedulers.io())
          .map { WordsResult.SelectWordSuccess }
      }
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