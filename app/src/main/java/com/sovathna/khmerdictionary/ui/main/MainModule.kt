package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.di.scope.MainScope
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.domain.model.intent.WordsIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class MainModule {

  @Provides
  @Named("instance")
  @MainScope
  fun viewModel(activity: MainActivity, factory: ViewModelProvider.Factory) =
    ViewModelProvider(activity, factory)[MainViewModel::class.java]

  @Provides
  @MainScope
  fun clickWordSubject() = PublishSubject.create<Event<Word>>()

  @Provides
  @MainScope
  fun selectWordSubject() = BehaviorSubject.create<WordsIntent.SelectWord>()

  @Provides
  @MainScope
  fun fabVisibilitySubject() = PublishSubject.create<Boolean>()

  @Provides
  @MainScope
  fun searchWordsIntent() = PublishSubject.create<SearchesIntent.GetWords>()

  @Provides
  @MainScope
  fun bookmarkedLiveData() = MutableLiveData<Boolean>()

  @Provides
  @MainScope
  fun menuItemClickLiveData() = MutableLiveData<Event<String>>()

  @Provides
  @MainScope
  fun recycledViewPool() = RecyclerView.RecycledViewPool()

  @Provides
  @MainScope
  fun clearHistoriesIntent() = PublishSubject.create<HistoriesIntent.ClearHistories>()

  @Provides
  @MainScope
  fun clearBookmarksIntent() = PublishSubject.create<BookmarksIntent.ClearBookmarks>()

  @Provides
  @MainScope
  @Named("clear_menu")
  fun clearMenuItemLiveData() = MutableLiveData<Boolean>()

}