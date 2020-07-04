package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
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

}