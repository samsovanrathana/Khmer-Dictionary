package com.sovathna.khmerdictionary.app

import android.content.Context
import android.content.SharedPreferences
import com.sovathna.khmerdictionary.data.local.AppPreferences
import com.sovathna.khmerdictionary.data.local.AppPreferencesImpl
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences =
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

  @Provides
  @Singleton
  fun appPreferences(pref: SharedPreferences): AppPreferences =
    AppPreferencesImpl(pref)

  @Provides
  @Singleton
  fun bookmarkMenuItemClickSubject() = PublishSubject.create<BookmarksIntent.UpdateBookmark>()
}