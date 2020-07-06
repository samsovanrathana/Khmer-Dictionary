package com.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState
import com.sovathna.androidmvi.viewmodel.BaseViewModel
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.data.local.AppPreferences
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import com.sovathna.khmerdictionary.domain.model.state.AbstractWordsState
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

abstract class AbstractWordsFragment<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>> :
  MviFragment<I, S, VM>(
    R.layout.fragment_word_list
  ) {

  @Inject
  protected lateinit var adapter: WordsAdapter

  private lateinit var layoutManager: LinearLayoutManager

  private var scrollChanged: ViewTreeObserver.OnScrollChangedListener? = null

  @Inject
  protected lateinit var clickWordSubject: PublishSubject<Event<Word>>

  @Inject
  protected lateinit var selectWordIntent: BehaviorSubject<WordsIntent.SelectWord>

  @Inject
  protected lateinit var appPref: AppPreferences

  @Inject
  protected lateinit var fabVisibilitySubject: PublishSubject<Boolean>

  @Inject
  protected lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    layoutManager = LinearLayoutManager(requireContext())
    rv.setRecycledViewPool(recycledViewPool)
    rv.setHasFixedSize(true)
    rv.layoutManager = layoutManager
    rv.adapter = adapter
    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        fabVisibilitySubject.onNext(dy <= 0)
      }
    })
  }

  override fun render(state: S) {
    with(state) {
      if (this is AbstractWordsState) {
        adapter.submitList(words)

        if (isMore) {
          addScrollChangedListener(words?.size ?: 0)
        } else {
          removeScrollChangedListener()
        }

        if (words?.isNotEmpty() == true) {
          adapter.setOnItemClickListener { _, item ->
            if (!item.isSelected) {
              clickWordSubject.onNext(Event(item.word))
            }
          }
          selectWordIntent.value?.word?.let {
            selectWordIntent.onNext(WordsIntent.SelectWord(it))
          }
        } else {
          adapter.setOnItemClickListener(null)
        }

        if (words?.isEmpty() == true) {
          vs_empty?.inflate()
          view?.findViewById<View>(R.id.layout_empty)?.visibility = View.VISIBLE
        } else {
          view?.findViewById<View>(R.id.layout_empty)?.visibility = View.GONE
        }
      }
    }
  }

  protected abstract fun onLoadMore(offset: Int, pageSize: Int = Const.PAGE_SIZE)

  private fun addScrollChangedListener(itemCount: Int) {
    removeScrollChangedListener()
    scrollChanged = ViewTreeObserver.OnScrollChangedListener {
      if (layoutManager.findLastVisibleItemPosition() +
        Const.LOAD_MORE_THRESHOLD >= itemCount
      ) {
        removeScrollChangedListener()
        onLoadMore(itemCount)
      }
    }
    rv?.viewTreeObserver?.addOnScrollChangedListener(scrollChanged)
  }

  private fun removeScrollChangedListener() {
    scrollChanged?.let {
      rv?.viewTreeObserver?.removeOnScrollChangedListener(it)
    }
  }

}