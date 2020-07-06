package com.sovathna.khmerdictionary.ui.words.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.postDelayed
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*

class HistoriesFragment :
  AbstractWordsFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

  private val getHistories = PublishSubject.create<HistoriesIntent.GetWords>()
  private val clearHistories = PublishSubject.create<HistoriesIntent.ClearHistories>()

  private var menuItemClear: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_words, menu)
    menuItemClear = menu.findItem(R.id.action_clear)
    menuItemClear?.isVisible =
      viewModel.stateLiveData.value?.words?.isNotEmpty() == true
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.action_clear) {
      clearHistories.onNext(HistoriesIntent.ClearHistories)
    }
    return super.onOptionsItemSelected(item)
  }

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
        menuItemClear?.isVisible = it.isNotEmpty()
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