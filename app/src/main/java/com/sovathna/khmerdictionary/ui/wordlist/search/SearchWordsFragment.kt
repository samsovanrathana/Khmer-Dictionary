package com.sovathna.khmerdictionary.ui.wordlist.search

import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SearchWordsFragment :
  WordListFragment<SearchWordsIntent, SearchWordsState, SearchWordsViewModel>() {

  @Inject
  lateinit var getWordsIntent: PublishSubject<SearchWordsIntent.GetWords>

  override fun intents(): Observable<SearchWordsIntent> =
    getWordsIntent
      .cast(SearchWordsIntent::class.java)

  override fun render(state: SearchWordsState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(
          SearchWordsIntent.GetWords(
            viewModel.searchTerm,
            0,
            Const.PAGE_SIZE,
            true
          )
        )
      }
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getWordsIntent.onNext(
      SearchWordsIntent.GetWords(
        viewModel.searchTerm,
        offset,
        pageSize,
        false
      )
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    Logger.d("destroy search")
  }
}