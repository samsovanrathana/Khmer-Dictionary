package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun bookmarkMenuItemClickIntent() = PublishSubject.create<BookmarksIntent.UpdateBookmark>()
}