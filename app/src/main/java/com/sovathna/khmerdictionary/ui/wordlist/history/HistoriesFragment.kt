package com.sovathna.khmerdictionary.ui.wordlist.history

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

class HistoriesFragment :
  WordListFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

  @Inject
  lateinit var getHistories: PublishSubject<HistoriesIntent.GetWords>

  @Inject
  lateinit var update: PublishSubject<HistoriesIntent.Update>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getHistories,
      update,
      selectedItemSubject
    )

  override fun onResume() {
    super.onResume()
    LiveDataReactiveStreams
      .fromPublisher(click.toFlowable(BackpressureStrategy.BUFFER))
      .observe(viewLifecycleOwner, Observer {
        update.onNext(HistoriesIntent.Update(it.peekContent()))
      })
  }

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
        rv.post {
          rv.scrollToPosition(0)
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