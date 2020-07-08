package com.sovathna.khmerdictionary.ui.words.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import com.sovathna.khmerdictionary.ui.words.WordsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WordsFragment :
  AbstractWordsFragment<WordsIntent, WordsState, WordsViewModel>() {

  override val viewModel: WordsViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<WordsIntent.GetWords>()

  private lateinit var pagingAdapter: WordsPagingAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    pagingAdapter = WordsPagingAdapter()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv.adapter = pagingAdapter
  }

  override fun intents(): Observable<WordsIntent> =
    Observable.merge(
      getWordsIntent,
      selectWordIntent
    )

  override fun render(state: WordsState) {
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(
          WordsIntent.GetWords(
            0,
            Const.PAGE_SIZE
          )
        )
      }
      wordsLiveData?.observe(viewLifecycleOwner, Observer {
        pagingAdapter.submitData(lifecycle, it)
      })
    }
  }

  override fun onLoadMore(offset: Int, pageSize: Int) {
    getWordsIntent.onNext(
      WordsIntent.GetWords(
        offset,
        pageSize
      )
    )
  }
}