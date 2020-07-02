package com.sovathna.khmerdictionary.ui.main

import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionModule
import com.sovathna.khmerdictionary.ui.wordlist.bookmark.BookmarksFragment
import com.sovathna.khmerdictionary.ui.wordlist.bookmark.BookmarksModule
import com.sovathna.khmerdictionary.ui.wordlist.history.HistoriesFragment
import com.sovathna.khmerdictionary.ui.wordlist.history.HistoriesModule
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.main.MainWordListModule
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsFragment
import com.sovathna.khmerdictionary.ui.wordlist.search.SearchWordsModule
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
      SearchWordsModule::class
    ]
  )
  abstract fun searchWordsFragment(): SearchWordsFragment

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