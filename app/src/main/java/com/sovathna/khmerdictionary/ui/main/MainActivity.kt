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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.livedata.EventObserver
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.listener.DrawerListener
import com.sovathna.khmerdictionary.ui.definition.DefinitionActivity
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.bookmark.BookmarksFragment
import com.sovathna.khmerdictionary.ui.wordlist.history.HistoriesFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
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

  @Inject
  lateinit var bookmarkedLiveData: MutableLiveData<Boolean>

  @Inject
  lateinit var menuItemClick: MutableLiveData<Event<String>>

  @Inject
  lateinit var selectedItemSubject: BehaviorSubject<MainWordListIntent.Selected>

  private var menu: Menu? = null
  private var searchItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    FirebaseInstanceId.getInstance().instanceId
      .addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
          Logger.e("getInstanceId failed")
          return@OnCompleteListener
        }

        val token = task.result?.token
        Logger.d("FCM Token: $token")
      })

    setSupportActionBar(toolbar)
    if (savedInstanceState == null)
      viewModel.title = getString(R.string.app_name_kh)

    viewModel.titleLiveData.observe(this, Observer(::setTitle))

    bookmarkedLiveData.observe(this, Observer { isBookmark ->
      menu?.findItem(R.id.action_bookmark)?.let { item ->
        when {
          isBookmark -> {
            item.title = "លុបការរក្សាទុក"
            item.icon = ContextCompat.getDrawable(
              this,
              R.drawable.round_bookmark_white_24
            )
          }
          else -> {
            item.title = "រក្សាទុក"
            item.icon = ContextCompat.getDrawable(
              this,
              R.drawable.round_bookmark_border_white_24
            )
          }
        }
      }
    })

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
      })

    fab.setOnClickListener {

      if (searchItem?.isActionViewExpanded == false) {
        if (supportFragmentManager.backStackEntryCount > 0) {
          viewModel.title = getString(R.string.app_name_kh)
          nav_view.checkedItem?.isChecked = false
          supportFragmentManager.popBackStack()
        }
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
        val searchView = searchItem?.actionView as? SearchView
        searchView?.let {
          if (it.query.isNotEmpty()) it.setQuery("", false)
          else searchItem?.collapseActionView()
        }
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
        menu.isChecked = true
        if (supportFragmentManager.backStackEntryCount > 0) {
          supportFragmentManager.popBackStack()
        }
        when (menu.itemId) {
          R.id.nav_histories -> {
            viewModel.title = getString(R.string.histories)
            supportFragmentManager.beginTransaction()
              .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
              )
              .replace(
                R.id.word_list_container,
                HistoriesFragment(),
                Const.HISTORIES_FRAGMENT_TAG
              )
              .addToBackStack(null)
              .commit()
          }
          R.id.nav_bookmarks -> {
            viewModel.title = getString(R.string.bookmarks)
            supportFragmentManager.beginTransaction()
              .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
              )
              .replace(
                R.id.word_list_container,
                BookmarksFragment(),
                Const.BOOKMARKS_FRAGMENT_TAG
              )
              .addToBackStack(null)
              .commit()
          }
        }
        return@setNavigationItemSelectedListener true
      }
      return@setNavigationItemSelectedListener false
    }
    drawer_layout.addDrawerListener(object : DrawerListener() {
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
    selectedItemSubject.onNext(MainWordListIntent.Selected(word))
    menu?.setGroupVisible(
      R.id.group_def,
      definition_container != null && selectedItemSubject.value?.word != null
    )
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
    if (requestCode == 0) {
      if (resultCode == Activity.RESULT_OK) {
        data?.let {
          it.getParcelableExtra<Word>("word")?.let { word ->
            click.onNext(Event(word))
          }
        }
      } else if (resultCode == Activity.RESULT_CANCELED) {
        selectedItemSubject.onNext(MainWordListIntent.Selected(null))
      }
      menu?.setGroupVisible(
        R.id.group_def,
        definition_container != null && selectedItemSubject.value?.word != null
      )
    }
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      if (supportFragmentManager.backStackEntryCount > 0) {
        viewModel.title = getString(R.string.app_name_kh)
        nav_view.checkedItem?.isChecked = false
        super.onBackPressed()
      } else {
        val defTmp = supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG)
        if (defTmp != null) {
          menu?.setGroupVisible(R.id.group_def, false)
          supportFragmentManager
            .beginTransaction()
            .remove(defTmp)
            .commit()
          selectedItemSubject.onNext(MainWordListIntent.Selected(null))
        } else {
          showCloseDialog()
        }
      }
    }
  }

  private var closeDialog: AlertDialog? = null

  private fun showCloseDialog() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Notice!")
    builder.setMessage("Do you really want to exit?")
    builder.setNegativeButton("No", null)
    builder.setPositiveButton("Yes") { _, _ ->
      finish()
    }
    closeDialog = builder.show()
  }

  override fun onPause() {
    super.onPause()
    closeDialog?.dismiss()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    this.menu = menu

    menu?.setGroupVisible(
      R.id.group_def,
      definition_container != null && selectedItemSubject.value?.word != null
    )

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
        viewModel.title = getString(R.string.app_name_kh)
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.action_bookmark) {
      menuItemClick.value = Event("bookmark")
    } else if (item.itemId == R.id.action_zoom_in) {
      menuItemClick.value = Event("zoom_in")
    } else if (item.itemId == R.id.action_zoom_out) {
      menuItemClick.value = Event("zoom_out")
    }
    return super.onOptionsItemSelected(item)
  }

}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)