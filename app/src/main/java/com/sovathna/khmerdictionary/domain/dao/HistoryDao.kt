package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.HistoryEntity
import io.reactivex.Single

@Dao
interface HistoryDao {
  @Query("SELECT * FROM history ORDER BY uid DESC LIMIT :offset, :pageSize")
  fun get(
    offset: Int,
    pageSize: Int
  ): Single<List<HistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(
    word: HistoryEntity
  ): Single<Long>

}