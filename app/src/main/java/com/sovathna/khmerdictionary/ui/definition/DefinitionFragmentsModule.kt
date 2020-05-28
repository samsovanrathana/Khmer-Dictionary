package com.sovathna.khmerdictionary.ui.definition

import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DefinitionFragmentsModule {
  @ContributesAndroidInjector(
    modules = [
      DefinitionModule::class
    ]
  )
  abstract fun definitionFragment(): DefinitionFragment
}