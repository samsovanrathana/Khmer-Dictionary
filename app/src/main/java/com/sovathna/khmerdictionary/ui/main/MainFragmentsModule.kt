package com.sovathna.khmerdictionary.ui.main

import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.definition.DefinitionModule
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {

  @ContributesAndroidInjector(
    modules = [
      MainWordListModule::class
    ]
  )
  abstract fun mainWordListFragment(): MainWordListFragment

  @ContributesAndroidInjector(
    modules = [
      DefinitionModule::class
    ]
  )
  abstract fun definitionFragment(): DefinitionFragment

}