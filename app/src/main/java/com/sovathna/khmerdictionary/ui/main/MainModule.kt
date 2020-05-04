package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.di.scope.MainScope
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class MainModule {

  @Provides
  @MainScope
  fun viewModel(activity: MainActivity) =
    ViewModelProvider(activity)[MainViewModel::class.java]

  @Provides
  @MainScope
  @Named("filter")
  fun filterIntent() = PublishSubject.create<WordListIntent.Filter>()

  @Provides
  @MainScope
  fun selectIntent() = PublishSubject.create<WordListIntent.Select>()
}