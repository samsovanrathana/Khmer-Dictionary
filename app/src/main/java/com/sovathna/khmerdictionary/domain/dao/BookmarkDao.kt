package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import io.reactivex.Single

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark LIMIT :offset, :pageSize")
  fun all(offset: Int, pageSize: Int): Single<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE word LIKE :searchTerm LIMIT :offset, :pageSize")
  fun search(searchTerm: String, offset: Int, pageSize: Int): Single<List<BookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: BookmarkEntity): Single<Long>

}