package com.sovathna.khmerdictionary.ui.wordlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_word_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class WordListFragment : MviFragment<WordListIntent, WordListState, WordListViewModel>(
  R.layout.fragment_word_list
), DrawerLayout.DrawerListener {

  @Inject
  @Named("filter")
  lateinit var filterIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  @Named("search")
  lateinit var searchIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

  @Inject
  lateinit var adapter: WordListAdapter

  @Inject
  lateinit var layoutManagerProvider: Provider<RecyclerView.LayoutManager>

  @Inject
  lateinit var mActivity: MainActivity

  private lateinit var layoutManager: LinearLayoutManager

  private var scrollChanged: ViewTreeObserver.OnScrollChangedListener? = null

  private var searchItem: MenuItem? = null

  private var oldTerm: String? = ""
  private var searchItemState: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    if (savedInstanceState != null) {
      oldTerm = savedInstanceState.getString("old_term")
      searchItemState = savedInstanceState.getBoolean("search_item_state")
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    LogUtil.i("fragment destroy")
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    layoutManager = layoutManagerProvider.get() as LinearLayoutManager
    rv.layoutManager = layoutManager
    rv.adapter = adapter

    mActivity.drawer_layout.addDrawerListener(this)
    mActivity.fab.show()
    mActivity.fab.setOnClickListener {
      if (searchItemState) {
        mActivity.fab?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.round_search_white_24
          )
        )
        searchItem?.isVisible = false
        searchItem?.collapseActionView()
      } else {
        mActivity.fab?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.round_clear_white_24
          )
        )
        searchItem?.isVisible = true
        searchItem?.expandActionView()
      }
    }
  }

  override fun onDestroyView() {
    mActivity.drawer_layout.removeDrawerListener(this)
    mActivity.fab.hide()
    super.onDestroyView()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("old_term", oldTerm)
    outState.putBoolean("search_item_state", searchItemState)
  }

  override fun onPause() {
    searchItemState = searchItem?.isActionViewExpanded == true
    super.onPause()
  }

  override fun intents(): Observable<WordListIntent> =
    Observable.merge(
      filterIntent,
      selectIntent,
      searchIntent.debounce(200, TimeUnit.MILLISECONDS)
    )

  override fun render(state: WordListState) {
    with(state) {

      if (isInit) filterIntent.onNext(WordListIntent.Filter(mActivity.getFilterType(), null, 0))


      if (isMore) {
        addScrollChangedListener(words?.size ?: 0)
      } else {
        removeScrollChangedListener()
      }

      if (words?.isNotEmpty() == true) {
        adapter.setOnItemClickListener { index ->
          if (!adapter.currentList[index].isSelected) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
              (searchItem?.actionView as? SearchView)?.setOnQueryTextListener(null)
//            searchItemState = searchItem?.isActionViewExpanded == true
            mActivity.onItemClick(adapter.currentList[index].word)

          }
        }
      } else {
        adapter.setOnItemClickListener(null)
      }

      adapter.submitList(words)

      if (words?.isEmpty() == true) {
        vs_empty?.inflate()
        view?.findViewById<View>(R.id.layout_empty)?.visibility = View.VISIBLE
      } else {
        view?.findViewById<View>(R.id.layout_empty)?.visibility = View.GONE
      }

      resetEvent?.getContentIfNotHandled()?.let {
        rv.postDelayed({
          rv.smoothScrollToPosition(0)
        }, 100)
      }

    }

  }

  private fun addScrollChangedListener(itemCount: Int) {
    removeScrollChangedListener()
    scrollChanged = ViewTreeObserver.OnScrollChangedListener {
      if (layoutManager.findLastVisibleItemPosition() + Const.LOAD_MORE_THRESHOLD >= itemCount) {
        filterIntent.onNext(WordListIntent.Filter(mActivity.getFilterType(), oldTerm, itemCount))
        LogUtil.i("load more. count: $itemCount")
        removeScrollChangedListener()
      }
    }
    rv?.viewTreeObserver?.addOnScrollChangedListener(scrollChanged)
  }

  private fun removeScrollChangedListener() {
    scrollChanged?.let {
      rv?.viewTreeObserver?.removeOnScrollChangedListener(it)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main, menu)
    LogUtil.i("onCreateOptionsMenu $oldTerm")
    searchItem = menu.findItem(R.id.action_search)
    searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        searchItemState = true
        mActivity.fab?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.round_clear_white_24
          )
        )
        searchItem?.isVisible = true
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        searchItemState = false
        mActivity.fab?.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.round_search_white_24
          )
        )
        searchItem?.isVisible = false
        return true
      }
    })
    if (searchItemState) searchItem?.expandActionView()
    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = "ស្វែងរកពាក្យ"
    searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)?.apply {
      textSize = 14.0F
      typeface = ResourcesCompat.getFont(requireContext(), R.font.kantumruy)
    }
    searchView.setQuery(oldTerm, false)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {
        LogUtil.i("query text changed: $newText")
        val searchTerm = newText?.trim()
        if (searchTerm != oldTerm) {
          LogUtil.i("search: $searchTerm")
          oldTerm = searchTerm
          searchIntent.onNext(
            WordListIntent.Filter(mActivity.getFilterType(), searchTerm, 0)
          )
          return true
        }
        return false
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
    })
  }

  override fun onDrawerStateChanged(newState: Int) {

  }

  override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

  }

  override fun onDrawerClosed(drawerView: View) {

  }

  override fun onDrawerOpened(drawerView: View) {
    searchItem?.collapseActionView()
  }
}