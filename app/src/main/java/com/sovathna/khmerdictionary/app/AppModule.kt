package com.sovathna.khmerdictionary.app

import android.content.Context
import com.sovathna.khmerdictionary.data.DataModule
import com.sovathna.khmerdictionary.vm.ViewModelsModule
import com.sovathna.khmerdictionary.vmfactory.ViewModelFactoryModule
import dagger.Module
import dagger.Provides
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
}