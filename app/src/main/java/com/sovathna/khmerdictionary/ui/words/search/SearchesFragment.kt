package com.sovathna.khmerdictionary.ui.words.search

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.domain.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.main.MainViewModel
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchesFragment :
  AbstractWordsFragment<MviIntent, SearchWordsState, SearchesViewModel>() {

  override val viewModel: SearchesViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<SearchesIntent.GetWords>()

  @Inject
  lateinit var mainGetWordsIntent: PublishSubject<SearchesIntent.GetWords>

  private val mainViewModel: MainViewModel by activityViewModels()

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getWordsIntent,
      mainGetWordsIntent.debounce(400, TimeUnit.MILLISECONDS),
      selectWordIntent
    )

  override fun render(state: SearchWordsState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(
          SearchesIntent.GetWords(
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
      SearchesIntent.GetWords(
        mainViewModel.searchTerm,
        offset,
        pageSize,
        false
      )
    )
  }
}