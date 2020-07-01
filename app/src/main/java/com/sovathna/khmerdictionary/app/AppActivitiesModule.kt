package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.di.scope.DefinitionScope
import com.sovathna.khmerdictionary.di.scope.MainScope
import com.sovathna.khmerdictionary.di.scope.SplashScope
import com.sovathna.khmerdictionary.ui.definition.DefinitionActivity
import com.sovathna.khmerdictionary.ui.definition.DefinitionModule
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.ui.main.MainFragmentsModule
import com.sovathna.khmerdictionary.ui.main.MainModule
import com.sovathna.khmerdictionary.ui.splash.SplashActivity
import com.sovathna.khmerdictionary.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppActivitiesModule {
  @ContributesAndroidInjector(
    modules = [
      SplashModule::class
    ]
  )
  @SplashScope
  abstract fun splashActivity(): SplashActivity

  @ContributesAndroidInjector(
    modules = [
      MainModule::class,
      MainFragmentsModule::class
    ]
  )
  @MainScope
  abstract fun mainActivity(): MainActivity

  @ContributesAndroidInjector(
    modules = [
      DefinitionModule::class
    ]
  )
  @DefinitionScope
  abstract fun definitionActivity(): DefinitionActivity
}