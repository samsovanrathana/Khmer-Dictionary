package com.sovathna.khmerdictionary.ui.words.history

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@AndroidEntryPoint
class HistoriesFragment :
  AbstractWordsFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

  override val viewModel: HistoriesViewModel by viewModels()

  private val getHistories = PublishSubject.create<HistoriesIntent.GetWords>()

  @Inject
  lateinit var clearHistories: PublishSubject<HistoriesIntent.ClearHistories>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getHistories,
      clearHistories,
      selectWordIntent
    )

  override fun render(state: HistoriesState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getHistories.onNext(
          HistoriesIntent.GetWords(
            0,
            Const.PAGE_SIZE
          )
        )
      }
      words?.let {
        clearMenuItemLiveData.value = it.isNotEmpty()
      }
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getHistories.onNext(
      HistoriesIntent.GetWords(
        offset,
        Const.PAGE_SIZE
      )
    )
  }

}