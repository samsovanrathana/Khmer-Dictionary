package com.sovathna.khmerdictionary.data.remote

import com.sovathna.khmerdictionary.domain.service.DownloadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {

  @Provides
  @Singleton
  fun downloadService(
    retrofit: Retrofit
  ): DownloadService =
    retrofit
      .create(DownloadService::class.java)
}