package com.sovathna.khmerdictionary.ui.words.history

import androidx.core.view.postDelayed
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

class HistoriesFragment :
  AbstractWordsFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

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
        rv.postDelayed(400) {
          rv.smoothScrollToPosition(0)
        }
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