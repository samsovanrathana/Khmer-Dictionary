package com.sovathna.khmerdictionary.app

import android.content.Context
import androidx.multidex.MultiDex
import com.sovathna.androidmvi.Logger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AndroidApp : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
    DaggerAppComponent
      .factory()
      .create(this)

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun onCreate() {
    super.onCreate()
    Logger.init()
  }

}