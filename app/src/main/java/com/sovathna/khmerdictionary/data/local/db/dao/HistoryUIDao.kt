package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import io.reactivex.Single

@Dao
interface HistoryUIDao {
  @Query("SELECT * FROM histories_ui ORDER BY uid DESC")
  fun get(): PagingSource<Int, HistoryUI>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addAll(words: List<HistoryUI>): Single<List<Long>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: HistoryUI): Single<Long>

  @Query("DELETE FROM histories_ui")
  fun deleteAll(): Single<Int>

  @Query("SELECT * FROM histories_ui WHERE isSelected = 1")
  fun getSelected(): Single<HistoryUI>

  @Query("UPDATE histories_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Single<Int>

  @Query("UPDATE histories_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Single<Int>
}