package com.sovathna.khmerdictionary.ui.definition

import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module(
  includes = [
    DefinitionFragmentsModule::class
  ]
)
class DefinitionModule {
  @Provides
  fun fab() = PublishSubject.create<Boolean>()
}