package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Single

@Dao
interface WordUIDao {
  @Query("SELECT * FROM words_ui ORDER BY name")
  fun get(): PagingSource<Int, WordUI>

  @Insert
  fun addAll(words: List<WordUI>): Single<List<Long>>

  @Query("DELETE FROM words_ui")
  fun deleteAll(): Single<Int>

  @Query("SELECT id FROM words_ui WHERE isSelected = 1")
  fun getSelected(): Single<Long>

  @Query("UPDATE words_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id:Long,isSelected: Boolean): Single<Int>
}