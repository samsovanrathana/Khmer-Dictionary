package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class InteractorModule {

  @Binds
  @ActivityRetainedScoped
  abstract fun splashInteractor(
    impl: SplashInteractorImpl
  ): SplashInteractor

  @Binds
  @ActivityRetainedScoped
  abstract fun searchesInteractor(
    impl: SearchesInteractorImpl
  ): SearchesInteractor

  @Binds
  @ActivityRetainedScoped
  abstract fun historiesInteractor(
    impl: HistoriesInteractorImpl
  ): HistoriesInteractor

  @Binds
  @ActivityRetainedScoped
  abstract fun bookmarksInteractor(
    impl: BookmarksInteractorImpl
  ): BookmarksInteractor

  @Binds
  @ActivityRetainedScoped
  abstract fun definitionInteractor(
    impl: DefinitionInteractorImpl
  ): DefinitionInteractor

}