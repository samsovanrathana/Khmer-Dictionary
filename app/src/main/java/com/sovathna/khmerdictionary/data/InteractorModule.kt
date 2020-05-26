package com.sovathna.khmerdictionary.data

import com.sovathna.khmerdictionary.data.interactor.DefinitionInteractorImpl
import com.sovathna.khmerdictionary.data.interactor.MainWordListInteractorImpl
import com.sovathna.khmerdictionary.data.interactor.SearchWordsInteractorImpl
import com.sovathna.khmerdictionary.data.interactor.SplashInteractorImpl
import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.interactor.MainWordListInteractor
import com.sovathna.khmerdictionary.domain.interactor.SearchWordsInteractor
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
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
    impl: MainWordListInteractorImpl
  ): MainWordListInteractor

  @Binds
  abstract fun searchWordsInteractor(
    impl: SearchWordsInteractorImpl
  ): SearchWordsInteractor

  @Binds
  abstract fun definitionInteractor(
    impl: DefinitionInteractorImpl
  ): DefinitionInteractor

}