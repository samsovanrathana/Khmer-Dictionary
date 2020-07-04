package com.sovathna.khmerdictionary.ui.words.search

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SearchesModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: SearchesFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[SearchesViewModel::class.java]

}