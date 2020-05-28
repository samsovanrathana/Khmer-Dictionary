package com.sovathna.khmerdictionary.ui.wordlist.history

import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class HistoriesFragment :
  WordListFragment<HistoriesIntent, HistoriesState, HistoriesViewModel>() {

  @Inject
  lateinit var getHistories: PublishSubject<HistoriesIntent.GetWords>

  override fun intents(): Observable<HistoriesIntent> =
    getHistories
      .cast(HistoriesIntent::class.java)

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