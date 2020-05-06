package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import io.reactivex.Single

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark ORDER BY id DESC LIMIT :offset, :pageSize")
  fun all(offset: Int, pageSize: Int): Single<List<BookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: BookmarkEntity): Single<Long>

  @Query("SELECT * FROM bookmark WHERE word_id=:wordId")
  fun get(wordId: Long): Single<BookmarkEntity>

  @Query("DELETE FROM bookmark WHERE word_id=:wordId")
  fun delete(wordId: Long): Single<Int>

}