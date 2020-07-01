package com.sovathna.khmerdictionary.ui.definition

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class DefinitionActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var bookmarkedLiveData: MutableLiveData<Boolean>

  @Inject
  lateinit var menuItemClick: MutableLiveData<Event<String>>

  private var menu: Menu? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_definition)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    title = "ពន្យល់ន័យ"

    intent.getParcelableExtra<Word>("word")?.let { word ->
      if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        setResult(Activity.RESULT_OK, Intent().apply {
          putExtra("word", word)
        })
        finish()
      }
      if (savedInstanceState == null) {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, DefinitionFragment().apply {
            arguments = Bundle().apply {
              putParcelable("word", word)
            }
          })
          .commit()
      }
    }

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

  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.definition_menu, menu)
    this.menu = menu
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) onBackPressed()
    else if (item.itemId == R.id.action_bookmark) {
      menuItemClick.value = Event("bookmark")
    }
    return super.onOptionsItemSelected(item)
  }

}