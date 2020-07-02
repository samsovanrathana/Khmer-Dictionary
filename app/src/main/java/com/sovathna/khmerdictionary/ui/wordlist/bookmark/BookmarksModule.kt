package com.sovathna.khmerdictionary.ui.wordlist.bookmark

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class BookmarksModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: BookmarksFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[BookmarksViewModel::class.java]

  @Provides
  fun getBookmarks() = PublishSubject.create<BookmarksIntent.GetWords>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}