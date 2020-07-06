package com.sovathna.khmerdictionary.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.sovathna.androidmvi.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApp : Application() {

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun onCreate() {
    super.onCreate()
    Logger.init()
  }

}