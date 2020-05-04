package com.sovathna.khmerdictionary.data

import android.content.Context
import androidx.room.Room
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

  @Provides
  @Singleton
  fun appDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, Const.DB_NAME)
      .fallbackToDestructiveMigration()
      .build()

  @Provides
  @Singleton
  fun localDatabase(context: Context):LocalDatabase =
    Room.databaseBuilder(context,LocalDatabase::class.java, "local.db")
      .fallbackToDestructiveMigration()
      .build()

}