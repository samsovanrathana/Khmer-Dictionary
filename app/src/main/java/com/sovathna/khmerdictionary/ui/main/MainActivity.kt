package com.sovathna.khmerdictionary.ui.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.livedata.EventObserver
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.ui.definition.DefinitionActivity
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsFragment
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var click: PublishSubject<Event<Word>>

  @Inject
  lateinit var search: PublishSubject<SearchWordsIntent.GetWords>

  @Inject
  @Named("instance")
  lateinit var viewModel: MainViewModel

  @Inject
  lateinit var fabVisibility: PublishSubject<Boolean>

  private var searchItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    title = getString(R.string.app_name_kh)

    LiveDataReactiveStreams
      .fromPublisher(
        click
          .throttleFirst(200, TimeUnit.MILLISECONDS)
          .replay(1)
          .autoConnect(0)
          .toFlowable(BackpressureStrategy.BUFFER)
      )
      .observe(this, EventObserver {
        onItemClick(it)
      }
      )

    fab.setOnClickListener {

      if (searchItem?.isActionViewExpanded == false) {
        searchItem?.expandActionView()
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
    drawerToggle.syncState()

    nav_view.setNavigationItemSelectedListener { menu ->
      drawer_layout.closeDrawer(GravityCompat.START)
      if (!menu.isChecked) {

        return@setNavigationItemSelectedListener true
      }
      return@setNavigationItemSelectedListener false
    }
    drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
      override fun onDrawerStateChanged(newState: Int) {

      }

      override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

      }

      override fun onDrawerClosed(drawerView: View) {

      }

      override fun onDrawerOpened(drawerView: View) {
        if (searchItem?.isActionViewExpanded == true) {
          searchItem?.collapseActionView()
        }
      }
    })

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        .replace(
          R.id.word_list_container,
          MainWordListFragment(),
          Const.WORD_LIST_FRAGMENT_TAG
        )
        .commit()
    } else {
      supportFragmentManager
        .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG)
        ?.let {
          it.arguments?.let { args ->
            args.getParcelable<Word>("word")?.let { word ->
              click.onNext(Event(word))
            }
          }
          supportFragmentManager
            .beginTransaction()
            .remove(it)
            .commit()
        }
    }

    LiveDataReactiveStreams
      .fromPublisher(fabVisibility.toFlowable(BackpressureStrategy.BUFFER))
      .observe(this, Observer {
        if (it) fab.show() else fab.hide()
      })

  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  private fun onItemClick(word: Word) {
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      val intent = Intent(this, DefinitionActivity::class.java)
      intent.putExtra("word", word)
      startActivityForResult(intent, 0)
    } else {
      supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
          R.anim.fade_in,
          R.anim.fade_out
        )
        .replace(
          R.id.definition_container,
          DefinitionFragment().apply {
            arguments = Bundle().apply {
              putParcelable("word", word)
            }
          },
          Const.DEFINITION_FRAGMENT_TAG
        )
        .commit()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
      data?.let {
        it.getParcelableExtra<Word>("word")?.let { word ->
          click.onNext(Event(word))
        }
      }
    }
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
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
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

        fab?.setImageDrawable(
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.round_search_white_24
          )
        )
        if (supportFragmentManager.backStackEntryCount > 0) {
          supportFragmentManager.popBackStack()
        }
        return true
      }
    })

    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = "ស្វែងរកពាក្យ"
    supportFragmentManager.findFragmentByTag(Const.SEARCH_WORDS_FRAGMENT_TAG)?.let {
      searchItem?.expandActionView()
      searchView.setQuery(viewModel.searchTerm, true)
    }
    searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)?.apply {
      textSize = 14.0F
      typeface = ResourcesCompat.getFont(this@MainActivity, R.font.kantumruy)
    }

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {

        val searchTerm = newText?.trim() ?: ""
        if (searchTerm != viewModel.searchTerm) {
          viewModel.searchTerm = searchTerm
          search.onNext(
            SearchWordsIntent.GetWords(
              viewModel.searchTerm,
              0,
              Const.PAGE_SIZE,
              true
            )
          )
        }
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