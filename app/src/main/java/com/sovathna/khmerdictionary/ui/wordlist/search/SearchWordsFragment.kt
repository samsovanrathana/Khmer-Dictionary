package com.sovathna.khmerdictionary.ui.wordlist.search

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.domain.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.main.MainViewModel
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class SearchWordsFragment :
  WordListFragment<MviIntent, SearchWordsState, SearchWordsViewModel>() {

  @Inject
  @Named("local")
  lateinit var getWordsIntent: PublishSubject<SearchWordsIntent.GetWords>

  @Inject
  lateinit var search: PublishSubject<SearchWordsIntent.GetWords>

  @Inject
  @Named("instance")
  lateinit var mainViewModel: MainViewModel

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getWordsIntent,
      search.debounce(400, TimeUnit.MILLISECONDS),
      selectedItemSubject
    )

  override fun render(state: SearchWordsState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(
          SearchWordsIntent.GetWords(
            "",
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
        mainViewModel.searchTerm,
        offset,
        pageSize,
        false
      )
    )
  }
}