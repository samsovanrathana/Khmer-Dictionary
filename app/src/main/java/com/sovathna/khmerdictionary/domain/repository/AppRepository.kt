package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.FilterType
import com.sovathna.khmerdictionary.domain.model.Word
import io.reactivex.Observable

interface AppRepository {

  fun getMainWordList(
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>>

  fun getDefinition(
    id: Long
  ): Observable<Definition>

  fun checkBookmark(
    id: Long
  ): Observable<Boolean>

  fun addBookmark(
    entity: BookmarkEntity
  ): Observable<Long>

  fun deleteBookmark(
    id: Long
  ): Observable<Int>
}