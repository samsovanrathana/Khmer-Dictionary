package com.sovathna.khmerdictionary.ui.definition.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.livedata.EventObserver
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.data.local.AppPreferences
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.state.DefinitionState
import dagger.Lazy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_definition.*
import javax.inject.Inject

class DefinitionFragment :
  MviFragment<DefinitionIntent, DefinitionState, DefinitionViewModel>(
    R.layout.fragment_definition
  ) {

  @Inject
  lateinit var getDefinitionIntent: PublishSubject<DefinitionIntent.GetDefinition>

  private var bookmark = PublishSubject.create<DefinitionIntent.AddDeleteBookmark>()

  @Inject
  lateinit var fabVisibility: Lazy<PublishSubject<Boolean>>

  @Inject
  lateinit var bookmarkedLiveData: MutableLiveData<Boolean>

  @Inject
  lateinit var menuItemClick: MutableLiveData<Event<String>>

  @Inject
  lateinit var appPref: AppPreferences

  private lateinit var word: Word

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      word = it.getParcelable("word")!!
      fabVisibility.get().onNext(true)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val textSize = appPref.getTextSize()
    tv_definition.textSize = textSize
    tv_name.textSize = textSize + 8.0F

    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      nsv.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
        if (scrollY < oldScrollY) {
          fabVisibility.get().onNext(true)
        } else if (scrollY > oldScrollY) {
          fabVisibility.get().onNext(false)
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    menuItemClick.observe(viewLifecycleOwner, EventObserver {
      when (it) {
        "bookmark" -> {
          bookmark.onNext(DefinitionIntent.AddDeleteBookmark(word))
        }
        "zoom_in" -> {
          val textSize = appPref.incrementTextSize()
          tv_definition.textSize = textSize
          tv_name.textSize = textSize + 8.0F
        }
        "zoom_out" -> {
          val textSize = appPref.decrementTextSize()
          tv_definition.textSize = textSize
          tv_name.textSize = textSize + 8.0F
        }
      }
    })
  }

  override fun intents(): Observable<DefinitionIntent> =
    Observable.merge(
      getDefinitionIntent,
      bookmark
    )

  override fun render(state: DefinitionState) {
    with(state) {
      if (isInit) {
        getDefinitionIntent.onNext(DefinitionIntent.GetDefinition(word))
      }

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
      }
      bookmarkedLiveData.value = isBookmark == true
    }
  }

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
        Logger.d("click: ${span?.url}")
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

}