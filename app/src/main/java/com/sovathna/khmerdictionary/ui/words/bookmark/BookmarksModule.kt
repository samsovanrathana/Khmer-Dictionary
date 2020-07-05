package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BookmarksModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: BookmarksFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[BookmarksViewModel::class.java]

}