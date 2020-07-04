package com.sovathna.khmerdictionary.ui.words.history

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HistoriesModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: HistoriesFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[HistoriesViewModel::class.java]

}