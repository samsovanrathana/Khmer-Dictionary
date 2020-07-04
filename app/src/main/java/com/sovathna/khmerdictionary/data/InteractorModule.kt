package com.sovathna.khmerdictionary.data

import com.sovathna.khmerdictionary.data.interactor.*
import com.sovathna.khmerdictionary.domain.interactor.*
import dagger.Binds
import dagger.Module

@Module
abstract class InteractorModule {

  @Binds
  abstract fun splashInteractor(
    impl: SplashInteractorImpl
  ): SplashInteractor

  @Binds
  abstract fun mainWordListInteractor(
    impl: WordsInteractorImpl
  ): WordsInteractor

  @Binds
  abstract fun searchWordsInteractor(
    impl: SearchesInteractorImpl
  ): SearchesInteractor

  @Binds
  abstract fun historiesInteractor(
    impl: HistoriesInteractorImpl
  ): HistoriesInteractor

  @Binds
  abstract fun bookmarksInteractor(
    impl: BookmarksInteractorImpl
  ): BookmarksInteractor

  @Binds
  abstract fun definitionInteractor(
    impl: DefinitionInteractorImpl
  ): DefinitionInteractor

}