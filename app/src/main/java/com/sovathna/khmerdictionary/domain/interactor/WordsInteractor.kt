package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.result.WordsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class WordsInteractor : MviInteractor<WordsIntent, WordsResult>() {

  abstract val getWords:
      ObservableTransformer<WordsIntent.GetWords, WordsResult>

  abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, WordsResult>

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