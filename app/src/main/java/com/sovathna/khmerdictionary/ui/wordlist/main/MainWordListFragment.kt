package com.sovathna.khmerdictionary.ui.wordlist.main

import androidx.lifecycle.Observer
import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.state.MainWordListState
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainWordListFragment :
  WordListFragment<MainWordListIntent, MainWordListState, MainWordListViewModel>() {

  @Inject
  lateinit var getWordListIntent: PublishSubject<MainWordListIntent.GetWordList>

  @Inject
  lateinit var selected: PublishSubject<MainWordListIntent.Selected>

  override fun intents(): Observable<MainWordListIntent> =
    Observable.merge(getWordListIntent, selected)

  override fun onResume() {
    super.onResume()
    selectedLiveData.observe(viewLifecycleOwner, Observer {
      Logger.d("$it")
      selected.onNext(MainWordListIntent.Selected(it))
    })
  }

  override fun render(state: MainWordListState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordListIntent.onNext(
          MainWordListIntent.GetWordList(
            0,
            Const.PAGE_SIZE
          )
        )
      }
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getWordListIntent.onNext(
      MainWordListIntent.GetWordList(
        offset,
        pageSize
      )
    )
  }
}