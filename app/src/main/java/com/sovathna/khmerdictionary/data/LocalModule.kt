package com.sovathna.khmerdictionary.data

import android.content.Context
import androidx.room.Room
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

  @Provides
  @Singleton
  fun appDatabase(@ApplicationContext context: Context): AppDatabase =
    Room
      .databaseBuilder(
        context,
        AppDatabase::class.java,
        Const.DB_NAME
      )
      .fallbackToDestructiveMigration()
      .build()

  @Provides
  @Singleton
  fun localDatabase(@ApplicationContext context: Context): LocalDatabase =
    Room
      .databaseBuilder(
        context,
        LocalDatabase::class.java,
        "local.db"
      )
      .fallbackToDestructiveMigration()
      .build()

}