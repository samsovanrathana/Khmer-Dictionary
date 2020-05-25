package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : DaggerAppCompatActivity() {

  lateinit var drawerToggle: ActionBarDrawerToggle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    title = getString(R.string.app_name_kh)

    drawerToggle = ActionBarDrawerToggle(
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
        updateTitle(menu.itemId)
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

    updateTitle(nav_view?.checkedItem?.itemId)

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
      val fragment = supportFragmentManager
        .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?
      fragment?.let {
        if (supportFragmentManager.backStackEntryCount > 0)
          supportFragmentManager.popBackStackImmediate()

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
            fragment,
            Const.DEFINITION_FRAGMENT_TAG
          )
          .addToBackStack(null)
          .commit()
      }

    }
  }

  private fun updateTitle(itemId: Int?) {
    title = when (itemId) {
      R.id.nav_bookmarks -> "បញ្ជីពាក្យរក្សាទុក"
      R.id.nav_histories -> "បញ្ជីពាក្យធ្លាប់មើល"
      else -> getString(R.string.app_name_kh)
    }
  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

//  fun onItemClick(word: Word) {
//    selectIntent.onNext(WordListIntent.Select(word.id))
//    val fragment = supportFragmentManager
//      .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as? DefinitionFragment
//
//    if (supportFragmentManager.backStackEntryCount > 0)
//      supportFragmentManager.popBackStackImmediate()
//
//    val tran = supportFragmentManager.beginTransaction()
//    if (definition_container != null) {
//      tran.setCustomAnimations(
//        R.anim.fade_in,
//        R.anim.fade_out
//      )
//    } else {
//      tran.setCustomAnimations(
//        R.anim.fade_in,
//        R.anim.fade_out,
//        R.anim.fade_in, R.anim.fade_out
//      )
//    }
//    tran
//      .replace(
//        if (definition_container != null) {
//          R.id.definition_container
//        } else {
//          R.id.word_list_container
//        },
//        (fragment ?: DefinitionFragment()).apply {
//          arguments = Bundle().apply {
//            putParcelable("word", word)
//          }
//        },
//        Const.DEFINITION_FRAGMENT_TAG
//      )
//      .addToBackStack(null)
//      .commit()
//
//  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      if (supportFragmentManager.backStackEntryCount > 0) {

        super.onBackPressed()
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