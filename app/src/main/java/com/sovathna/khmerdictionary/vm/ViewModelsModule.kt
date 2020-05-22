package com.sovathna.khmerdictionary.vm

import androidx.lifecycle.ViewModel
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.ui.definition.DefinitionViewModel
import com.sovathna.khmerdictionary.ui.splash.SplashViewModel
import com.sovathna.khmerdictionary.ui.wordlist.WordListViewModel
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
  @ViewModelKey(WordListViewModel::class)
  abstract fun wordListViewModel(
    vm: WordListViewModel
  ): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(DefinitionViewModel::class)
  abstract fun definitionViewModel(
    vm: DefinitionViewModel
  ): ViewModel
}

