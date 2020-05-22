package com.sovathna.khmerdictionary.ui.wordlist.main

import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainWordListFragment : WordListFragment<MainWordListIntent>() {

  lateinit var getWordsIntent: PublishSubject<MainWordListIntent.GetWords>

  override fun intents(): Observable<MainWordListIntent> =
    getWordsIntent.cast(MainWordListIntent::class.java)

  override fun render(state: WordListState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(MainWordListIntent.GetWords)
      }
    }
  }
}