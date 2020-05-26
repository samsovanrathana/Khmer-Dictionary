package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var click: PublishSubject<Word>

  @Inject
  lateinit var search: PublishSubject<SearchWordsIntent.GetWords>

  @Inject
  @Named("instance")
  lateinit var viewModel: MainViewModel

  private var searchItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    title = getString(R.string.app_name_kh)

    viewModel.pageLiveData.observe(this, Observer {
      when (it) {
        "home" -> {
          title = getString(R.string.app_name_kh)
        }
        "search" -> {
          title = "ស្វែងរកពាក្យ"

        }
        "history" -> {
          title = "ពាក្យធ្លាប់មើល"
        }
        "bookmark" -> {
          title = "ពាក្យរក្សាទុក"
        }
      }
    })

    fab.setOnClickListener {

      if (searchItem?.isActionViewExpanded == false) {
        searchItem?.expandActionView()
        viewModel.page.onNext("search")
        if (supportFragmentManager.backStackEntryCount > 0) {
          supportFragmentManager.popBackStack()
        }
        supportFragmentManager.beginTransaction()
          .setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
          )
          .replace(
            R.id.word_list_container,
            SearchWordsFragment(),
            Const.SEARCH_WORDS_FRAGMENT_TAG
          )
          .addToBackStack(null)
          .commit()
      } else {
        searchItem?.collapseActionView()

      }

    }

    val drawerToggle = ActionBarDrawerToggle(
      this,
      drawer_layout,
      toolbar,
      R.string.nav_open,
      R.string.nav_close
    )
    drawerToggle.setToolbarNavigationClickListener {
      if (!drawerToggle.isDrawerIndicatorEnabled) {
        onBackPressed()
      }
    }
    drawerToggle.syncState()

    nav_view.setNavigationItemSelectedListener { menu ->
      drawer_layout.closeDrawer(GravityCompat.START)
      if (!menu.isChecked) {
//        filterIntent.onNext(
//          WordListIntent.Filter(
//            when (menu.itemId) {
//              R.id.nav_bookmarks -> FilterType.Bookmark
//              R.id.nav_histories -> FilterType.History
//              else -> FilterType.All
//            }, null, 0
//          )
//        )
        return@setNavigationItemSelectedListener true
      }
      return@setNavigationItemSelectedListener false
    }

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        .replace(
          R.id.word_list_container,
          MainWordListFragment(),
          Const.WORD_LIST_FRAGMENT_TAG
        )
        .commit()
    }
//    else {
//      val fragment = supportFragmentManager
//        .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?
//      fragment?.let {
//        if (supportFragmentManager.backStackEntryCount > 0) {
//          supportFragmentManager.popBackStackImmediate()
//        }
//
//        val tran = supportFragmentManager.beginTransaction()
//        if (definition_container != null) {
//          tran.setCustomAnimations(
//            R.anim.fade_in,
//            R.anim.fade_out
//          )
//        } else {
//          tran.setCustomAnimations(
//            R.anim.fade_in,
//            R.anim.fade_out,
//            R.anim.fade_in,
//            R.anim.fade_out
//          )
//        }
//
//        tran
//          .replace(
//            if (definition_container != null) {
//              R.id.definition_container
//            } else {
//              R.id.word_list_container
//            },
//            fragment,
//            Const.DEFINITION_FRAGMENT_TAG
//          )
//          .addToBackStack(null)
//          .commit()
//      }
//
//    }

    LiveDataReactiveStreams
      .fromPublisher(
        click
          .throttleFirst(200, TimeUnit.MILLISECONDS)
          .toFlowable(BackpressureStrategy.BUFFER)
      )
      .observe(this, Observer {
        onItemClick(it)
      }
      )
  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  private fun onItemClick(word: Word) {
    val fragment = supportFragmentManager
      .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as? DefinitionFragment

    fragment?.let {
      if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStackImmediate()
    }

    val tran = supportFragmentManager.beginTransaction()
    if (definition_container != null) {
      tran.setCustomAnimations(
        R.anim.fade_in,
        R.anim.fade_out
      )
    } else {
      tran.setCustomAnimations(
        R.anim.fade_in,
        R.anim.fade_out,
        R.anim.fade_in,
        R.anim.fade_out
      )
    }
    tran
      .replace(
        if (definition_container != null) {
          R.id.definition_container
        } else {
          R.id.word_list_container
        },
        DefinitionFragment.newInstance(
          Bundle().apply {
            putParcelable("word", word)
          }
        ),
        Const.DEFINITION_FRAGMENT_TAG
      )
      .commit()
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {

        val fragment = supportFragmentManager
          .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?
        if (fragment != null) {
          supportFragmentManager.beginTransaction().remove(fragment).commit()
        } else {
          if (nav_view?.checkedItem?.isChecked == true) {
            nav_view?.checkedItem?.isChecked = false
            title = getString(R.string.app_name_kh)
          } else {
            super.onBackPressed()
          }
        }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    searchItem = menu?.findItem(R.id.action_search)
    searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        fab?.setImageDrawable(
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.round_clear_white_24
          )
        )
        searchItem?.isVisible = true
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

        fab?.setImageDrawable(
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.round_search_white_24
          )
        )
        searchItem?.isVisible = false
        if (supportFragmentManager.backStackEntryCount > 0) {
          supportFragmentManager.popBackStack()
        }
        return true
      }
    })

    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = "ស្វែងរកពាក្យ"
    searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)?.apply {
      textSize = 14.0F
      typeface = ResourcesCompat.getFont(this@MainActivity, R.font.kantumruy)
    }

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {

        val searchTerm = newText?.trim()

        search.onNext(
          SearchWordsIntent.GetWords(
            searchTerm ?: "",
            0,
            Const.PAGE_SIZE,
            true
          )
        )
        return true
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
    })
    return true
  }

//  override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    if (item.itemId == android.R.id.home) {
//      onBackPressed()
//    }
//    LogUtil.i("options click")
//    return super.onOptionsItemSelected(item)
//  }

}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)