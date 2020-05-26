package com.sovathna.khmerdictionary.ui.wordlist

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.state.MviState
import com.sovathna.androidmvi.viewmodel.BaseViewModel
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject
import javax.inject.Provider

abstract class WordListFragment<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>> :
  MviFragment<I, S, VM>(
    R.layout.fragment_word_list
  ) {

  @Inject
  protected lateinit var adapter: WordListAdapter

  @Inject
  protected lateinit var layoutManagerProvider:
      Provider<RecyclerView.LayoutManager>

  private lateinit var layoutManager: LinearLayoutManager

  private var scrollChanged: ViewTreeObserver.OnScrollChangedListener? = null

  @Inject
  lateinit var click: PublishSubject<Word>

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    layoutManager = layoutManagerProvider.get() as LinearLayoutManager
    rv.layoutManager = layoutManager
    rv.adapter = adapter
  }

  override fun render(state: S) {
    with(state) {
      if (this is WordListState) {
        adapter.submitList(words)

        if (isMore) {
          addScrollChangedListener(words?.size ?: 0)
        } else {
          removeScrollChangedListener()
        }

        if (words?.isNotEmpty() == true) {
          adapter.setOnItemClickListener { index ->
            with(adapter.currentList[index]) {
              if (!isSelected) click.onNext(word)
            }
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