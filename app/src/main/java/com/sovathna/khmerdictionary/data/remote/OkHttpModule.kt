package com.sovathna.khmerdictionary.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object OkHttpModule {

  @Provides
  @Singleton
  fun downloadOkHttp(): OkHttpClient =
    OkHttpClient()
      .newBuilder()
      .connectTimeout(1, TimeUnit.MINUTES)
      .readTimeout(0, TimeUnit.MINUTES)
      .build()

}