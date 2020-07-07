package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.reactivex.Single

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark ORDER BY uid DESC LIMIT :offset, :pageSize")
  fun get(
    offset: Int,
    pageSize: Int
  ): Single<List<BookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(
    word: BookmarkEntity
  ): Single<Long>

  @Query("SELECT * FROM bookmark WHERE id=:wordId")
  fun get(
    wordId: Long
  ): Single<BookmarkEntity>

  @Query("DELETE FROM bookmark WHERE id=:wordId")
  fun delete(
    wordId: Long
  ): Single<Int>

  @Query("DELETE FROM bookmark")
  fun clear(): Single<Int>
}