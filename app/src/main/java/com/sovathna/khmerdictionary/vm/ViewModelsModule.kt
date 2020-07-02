package com.sovathna.khmerdictionary.vm

import androidx.lifecycle.ViewModel
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionViewModel
import com.sovathna.khmerdictionary.ui.main.MainViewModel
import com.sovathna.khmerdictionary.ui.splash.SplashViewModel
import com.sovathna.khmerdictionary.ui.wordlist.bookmark.BookmarksViewModel
import com.sovathna.khmerdictionary.ui.wordlist.history.HistoriesViewModel
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListViewModel
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {
  @Binds
  @IntoMap
  @ViewModelKey(SplashViewModel::class)
  abstract fun splashViewModel(
    vm: SplashViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel::class)
  abstract fun mainViewModel(
    vm: MainViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MainWordListViewModel::class)
  abstract fun wordListViewModel(
    vm: MainWordListViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SearchWordsViewModel::class)
  abstract fun searchWordsViewModel(
    vm: SearchWordsViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(HistoriesViewModel::class)
  abstract fun historiesViewModel(
    vm: HistoriesViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(BookmarksViewModel::class)
  abstract fun bookmarksViewModel(
    vm: BookmarksViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(DefinitionViewModel::class)
  abstract fun definitionViewModel(
    vm: DefinitionViewModel
  ): ViewModel
}

