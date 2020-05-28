package com.sovathna.khmerdictionary.ui.definition

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.app_bar_main.*

class DefinitionActivity : DaggerAppCompatActivity() {

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

  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) finish()
    return super.onOptionsItemSelected(item)
  }

}