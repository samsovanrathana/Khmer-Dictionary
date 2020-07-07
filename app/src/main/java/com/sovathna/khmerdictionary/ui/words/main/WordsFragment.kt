package com.sovathna.khmerdictionary.ui.words.main

import androidx.fragment.app.viewModels
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

@AndroidEntryPoint
class WordsFragment :
  AbstractWordsFragment<WordsIntent, WordsState, WordsViewModel>() {

  override val viewModel: WordsViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<WordsIntent.GetWords>()

  override fun intents(): Observable<WordsIntent> =
    Observable.merge(
      getWordsIntent,
      selectWordIntent
    )

  override fun render(state: WordsState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(
          WordsIntent.GetWords(
            0,
            Const.PAGE_SIZE
          )
        )
      }
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getWordsIntent.onNext(
      WordsIntent.GetWords(
        offset,
        pageSize
      )
    )
  }
}