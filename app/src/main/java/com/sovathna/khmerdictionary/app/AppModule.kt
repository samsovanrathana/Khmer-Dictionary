package com.sovathna.khmerdictionary.app

import android.content.Context
import android.content.SharedPreferences
import com.sovathna.khmerdictionary.data.DataModule
import com.sovathna.khmerdictionary.data.local.AppPreferences
import com.sovathna.khmerdictionary.data.local.AppPreferencesImpl
import com.sovathna.khmerdictionary.domain.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.vm.ViewModelsModule
import com.sovathna.khmerdictionary.vmfactory.ViewModelFactoryModule
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module(
  includes = [
    AppActivitiesModule::class,
    DataModule::class,
    ViewModelsModule::class,
    ViewModelFactoryModule::class
  ]
)
object AppModule {
  @Provides
  @JvmStatic
  @Singleton
  fun context(app: AndroidApp): Context = app

  @Provides
  @JvmStatic
  @Singleton
  fun sharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

  @Provides
  @JvmStatic
  @Singleton
  fun appPreferences(pref: SharedPreferences): AppPreferences =
    AppPreferencesImpl(pref)

  @Provides
  @JvmStatic
  @Singleton
  fun bookmarkMenuItemClickSubject() = PublishSubject.create<BookmarksIntent.UpdateBookmark>()
}