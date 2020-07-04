package com.sovathna.khmerdictionary.ui.main

import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionModule
import com.sovathna.khmerdictionary.ui.words.bookmark.BookmarksFragment
import com.sovathna.khmerdictionary.ui.words.bookmark.BookmarksModule
import com.sovathna.khmerdictionary.ui.words.history.HistoriesFragment
import com.sovathna.khmerdictionary.ui.words.history.HistoriesModule
import com.sovathna.khmerdictionary.ui.words.main.WordsFragment
import com.sovathna.khmerdictionary.ui.words.main.WordsModule
import com.sovathna.khmerdictionary.ui.words.search.SearchesFragment
import com.sovathna.khmerdictionary.ui.words.search.SearchesModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {
  @ContributesAndroidInjector(
    modules = [
      WordsModule::class
    ]
  )
  abstract fun mainWordListFragment(): WordsFragment

  @ContributesAndroidInjector(
    modules = [
      SearchesModule::class
    ]
  )
  abstract fun searchWordsFragment(): SearchesFragment

  @ContributesAndroidInjector(
    modules = [
      HistoriesModule::class
    ]
  )
  abstract fun historiesFragment(): HistoriesFragment

  @ContributesAndroidInjector(
    modules = [
      BookmarksModule::class
    ]
  )
  abstract fun bookmarksFragment(): BookmarksFragment

  @ContributesAndroidInjector(
    modules = [
      DefinitionModule::class
    ]
  )
  abstract fun definitionFragment(): DefinitionFragment
}