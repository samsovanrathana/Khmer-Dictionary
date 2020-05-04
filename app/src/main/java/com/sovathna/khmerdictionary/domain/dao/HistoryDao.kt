package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.HistoryEntity
import io.reactivex.Single

@Dao
interface HistoryDao {
  @Query("SELECT * FROM history LIMIT :offset, :pageSize")
  fun all(offset: Int, pageSize: Int): Single<List<HistoryEntity>>

  @Query("SELECT * FROM history WHERE word LIKE :searchTerm LIMIT :offset, :pageSize")
  fun search(searchTerm: String, offset: Int, pageSize: Int): Single<List<HistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: HistoryEntity): Single<Long>

}