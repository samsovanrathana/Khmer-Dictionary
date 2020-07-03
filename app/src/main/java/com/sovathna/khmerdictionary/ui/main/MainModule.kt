package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.di.scope.MainScope
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
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
  fun click() = PublishSubject.create<Event<Word>>()

  @Provides
  @MainScope
  fun selectedItemSubject() = BehaviorSubject.create<MainWordListIntent.Selected>()

  @Provides
  @MainScope
  fun fab() = PublishSubject.create<Boolean>()

  @Provides
  @MainScope
  fun searchWordsIntent() = PublishSubject.create<SearchWordsIntent.GetWords>()

  @Provides
  @MainScope
  fun bookmarkedLiveData() = MutableLiveData<Boolean>()

  @Provides
  @MainScope
  fun menuItemClickLiveData() = MutableLiveData<Event<String>>()

}