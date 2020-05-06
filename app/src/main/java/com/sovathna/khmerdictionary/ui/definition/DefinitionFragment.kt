package com.sovathna.khmerdictionary.ui.definition

import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.state.DefinitionState
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_definition.*
import javax.inject.Inject

class DefinitionFragment:
  MviFragment<DefinitionIntent, DefinitionState, DefinitionViewModel>(
    R.layout.fragment_definition
  ) {

  companion object {
    fun newInstance(arguments: Bundle? = null): DefinitionFragment =
      DefinitionFragment().apply {
        this.arguments = arguments
      }
  }

  @Inject
  lateinit var getDefinitionIntent: PublishSubject<DefinitionIntent.Get>

  @Inject
  lateinit var bookmarkIntent: PublishSubject<DefinitionIntent.Bookmark>

  @Inject
  lateinit var mActivity: MainActivity

  private lateinit var word: Word
  private lateinit var bookmarkItem: MenuItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    arguments?.let {
      word = it.getParcelable("word")!!
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      mActivity.title = "ពន្យល់ន័យ"
    } else {
      mActivity.title = getString(R.string.app_name_kh)
    }
    mActivity.drawerToggle.isDrawerIndicatorEnabled =
      resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT
    mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(!mActivity.drawerToggle.isDrawerIndicatorEnabled)
    if (!mActivity.drawerToggle.isDrawerIndicatorEnabled) {
      mActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

  }

  override fun onDestroyView() {
    super.onDestroyView()
    mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    mActivity.drawerToggle.isDrawerIndicatorEnabled = true
    mActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    mActivity.title = getString(R.string.app_name_kh)
  }

  override fun intents(): Observable<DefinitionIntent> =
    Observable.merge(
      getDefinitionIntent,
      bookmarkIntent
    )

  override fun render(state: DefinitionState) {
    with(state) {
      LogUtil.i("definition state: $this")
      if (isInit) getDefinitionIntent.onNext(DefinitionIntent.Get(word.id))
      definition?.let {
        tv_name.text = definition.word

        val tmp = definition.definition.replace("<\"", "<a href=\"")
          .replace("/a", "</a>")
          .replace("\\n", "<br><br>")
          .replace(" : ", " : ឧ. ")
          .replace("ន.", "<span style=\"color:#D50000\">ន.</span>")
          .replace("កិ. វិ.", "<span style=\"color:#D50000\">កិ. វិ.</span>")
          .replace("កិ.វិ.", "<span style=\"color:#D50000\">កិ.វិ.</span>")
          .replace("កិ.", "<span style=\"color:#D50000\">កិ.</span>")
          .replace("និ.", "<span style=\"color:#D50000\">និ.</span>")
          .replace("គុ.", "<span style=\"color:#D50000\">គុ.</span>")
        setTextViewHTML(tv_definition, tmp)
//        tv_definition.text = definition.definition

        if (::bookmarkItem.isInitialized) {
          if (isBookmark == true) {
            bookmarkItem.icon =
              ContextCompat.getDrawable(requireContext(), R.drawable.round_bookmark_white_24)
            bookmarkItem.title = getString(R.string.delete_bookmark)
          } else {
            bookmarkItem.icon =
              ContextCompat.getDrawable(requireContext(), R.drawable.round_bookmark_border_white_24)
            bookmarkItem.title = getString(R.string.bookmark)
          }
        }
        this@DefinitionFragment.isBookmark = isBookmark
      }
    }
  }

  private var isBookmark: Boolean? = null

  private fun makeLinkClickable(
    strBuilder: SpannableStringBuilder,
    span: URLSpan?
  ) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
      override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
      }

      override fun onClick(widget: View) {
        LogUtil.i("click: ${span?.url}")
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

  private fun setTextViewHTML(text: TextView, html: String) {
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls =
      strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
      makeLinkClickable(strBuilder, span)
    }
    text.text = strBuilder
    text.movementMethod = LinkMovementMethod.getInstance()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.definition_menu, menu)
    bookmarkItem = menu.findItem(R.id.action_bookmark)
    if (isBookmark == true) {
      bookmarkItem.icon =
        ContextCompat.getDrawable(requireContext(), R.drawable.round_bookmark_white_24)
      bookmarkItem.title = getString(R.string.delete_bookmark)
    } else {
      bookmarkItem.icon =
        ContextCompat.getDrawable(requireContext(), R.drawable.round_bookmark_border_white_24)
      bookmarkItem.title = getString(R.string.bookmark)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_bookmark -> {
        bookmarkIntent.onNext(DefinitionIntent.Bookmark(word))
      }
    }
    return super.onOptionsItemSelected(item)
  }
}