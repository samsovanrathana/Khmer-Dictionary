package com.sovathna.khmerdictionary.ui.words.main

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class WordsModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: WordsFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[WordsViewModel::class.java]

}