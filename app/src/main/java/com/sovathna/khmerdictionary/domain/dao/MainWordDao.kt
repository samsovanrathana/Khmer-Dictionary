package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.MainWordEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MainWordDao {
  @Query("SELECT * FROM main_words")
  fun getWords(): Observable<List<MainWordEntity>>

  @Insert
  fun addWords(entities: List<MainWordEntity>): Completable

  @Query("DELETE FROM main_words WHERE 1=1")
  fun clearTable(): Completable
}