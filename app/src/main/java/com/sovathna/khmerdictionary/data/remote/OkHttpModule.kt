package com.sovathna.khmerdictionary.data.remote

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class OkHttpModule {

  @Provides
  @Singleton
  fun downloadOkHttp(): OkHttpClient =
    OkHttpClient()
      .newBuilder()
      .connectTimeout(1, TimeUnit.MINUTES)
      .readTimeout(0, TimeUnit.MINUTES)
      .build()

}